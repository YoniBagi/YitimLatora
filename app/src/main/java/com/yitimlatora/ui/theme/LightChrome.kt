package com.yitimlatora.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val LocalAppDarkTheme = staticCompositionLocalOf { false }

@Composable
fun goldCardBorder(): BorderStroke? =
    if (LocalAppDarkTheme.current) null else BorderStroke(2.dp, TorahGoldBorder)

@Composable
fun Modifier.goldBarBorder(): Modifier =
    if (LocalAppDarkTheme.current) this else border(2.dp, TorahGoldBorder)
