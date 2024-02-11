package team.returm.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val NAVIGATION_MYPAGE = "mypage"

fun NavGraphBuilder.mypage(){
    composable(NAVIGATION_MYPAGE) {
        mypage()
    }
}
