package com.uson.baselineprofile;

import androidx.benchmark.macro.junit4.BaselineProfileRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Until;

import kotlin.Unit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This test class generates a basic startup baseline profile for the target package.
 * <p>
 * We recommend you start with this but add important user flows to the profile to improve their performance.
 * Refer to the <a href="https://d.android.com/topic/performance/baselineprofiles">baseline profile documentation</a>
 * for more information.
 * <p>
 * You can run the generator with the "Generate Baseline Profile" run configuration in Android Studio or
 * the equivalent {@code generateBaselineProfile} gradle task:
 * <pre>
 * ./gradlew :app:generateReleaseBaselineProfile
 * </pre>
 * The run configuration runs the Gradle task and applies filtering to run only the generators.
 * <p>
 * Check <a href="https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args">documentation</a>
 * for more information about instrumentation arguments.
 * <p>
 * After you run the generator, you can verify the improvements running the {@link StartupBenchmarks} benchmark.
 * <p>
 * When using this class to generate a baseline profile, only API 33+ or rooted API 28+ are supported.
 * <p>
 * The minimum required version of androidx.benchmark to generate a baseline profile is 1.2.0.
 **/
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BaselineProfileGenerator {
    @Rule
    public BaselineProfileRule baselineProfileRule = new BaselineProfileRule();

    // 앱 스타트업 최적화: Splash → Landing 화면까지
    @Test
    public void generateStartupProfile() {
        baselineProfileRule.collect(
                /* packageName = */ "team.retum.jobis",
                /* maxIterations = */ 10,
                /* stableIterations = */ 3,
                /* outputFilePrefix = */ null,
                /* includeInStartupProfile = */ true,
                scope -> {
                    scope.pressHome();
                    scope.startActivityAndWait();

                    // Landing 화면 버튼이 실제로 렌더링될 때까지 대기
                    scope.getDevice().wait(Until.hasObject(By.text("새 계정으로 시작하기")), 10_000);
                    scope.getDevice().waitForIdle();

                    return Unit.INSTANCE;
                });
    }

    // Landing 인터랙션 최적화: 버튼 클릭 → SignIn 화면 전환
    @Test
    public void generateLandingInteractionProfile() {
        baselineProfileRule.collect(
                /* packageName = */ "team.retum.jobis",
                /* maxIterations = */ 8,
                /* stableIterations = */ 2,
                /* outputFilePrefix = */ null,
                /* includeInStartupProfile = */ false,
                scope -> {
                    scope.pressHome();
                    scope.startActivityAndWait();

                    boolean landingLoaded = scope.getDevice().wait(
                            Until.hasObject(By.text("기존 계정으로 로그인하기")), 10_000);
                    if (!landingLoaded) return Unit.INSTANCE;
                    scope.getDevice().waitForIdle();

                    // 로그인 화면 진입해 SignIn Composable 트리 미리 컴파일
                    scope.getDevice().findObject(By.text("기존 계정으로 로그인하기")).click();
                    scope.getDevice().wait(Until.hasObject(By.text("로그인")), 5_000);
                    scope.getDevice().waitForIdle();

                    return Unit.INSTANCE;
                });
    }
}