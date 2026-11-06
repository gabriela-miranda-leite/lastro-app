package com.lastro.core.ui.icon

import androidx.compose.material3.Icon
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.lastro.core.ui.components.IconWrapVariant
import com.lastro.core.ui.components.LastroIconWrap
import com.lastro.core.ui.theme.LastroTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
class LastroIconsTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val allIcons = listOf(
        LastroIcons.Dashboard,
        LastroIcons.List,
        LastroIcons.UserCircle,
        LastroIcons.ArrowLeft,
        LastroIcons.ChevronRight,
        LastroIcons.Plus,
        LastroIcons.X,
        LastroIcons.Mail,
        LastroIcons.Lock,
        LastroIcons.LockOpen,
        LastroIcons.Eye,
        LastroIcons.EyeOff,
        LastroIcons.User,
        LastroIcons.ArrowUp,
        LastroIcons.ArrowDown,
        LastroIcons.ArrowsTransfer,
        LastroIcons.Calendar,
        LastroIcons.Tag,
        LastroIcons.Note,
        LastroIcons.CurrencyDollar,
        LastroIcons.Repeat,
        LastroIcons.Trash,
        LastroIcons.Pencil,
        LastroIcons.Home,
        LastroIcons.Sparkles,
        LastroIcons.TrendingUp,
        LastroIcons.ChartPie,
        LastroIcons.ShoppingCart,
        LastroIcons.DeviceMobile,
        LastroIcons.Settings,
        LastroIcons.Moon,
        LastroIcons.Sun,
        LastroIcons.Bell,
        LastroIcons.Shield,
        LastroIcons.Logout,
        LastroIcons.Download,
        LastroIcons.Users,
        LastroIcons.Heart,
        LastroIcons.Link,
        LastroIcons.Copy,
        LastroIcons.Flag,
        LastroIcons.PiggyBank,
        LastroIcons.Check,
        LastroIcons.AlertCircle,
        LastroIcons.CheckCircle,
        LastroIcons.InfoCircle,
        LastroIcons.Loader,
        LastroIcons.WifiOff,
    )

    @Test
    fun all_icons_render_without_crash() {
        allIcons.forEach { res ->
            composeRule.setContent {
                LastroTheme {
                    Icon(
                        painter = painterResource(res),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            composeRule.onRoot().assertIsDisplayed()
        }
    }

    @Test
    fun icon_wrap_renders_all_variants_dark() {
        composeRule.setContent {
            LastroTheme(darkTheme = true) {
                IconWrapVariant.entries.forEach { variant ->
                    LastroIconWrap(
                        painter = painterResource(LastroIcons.Heart),
                        variant = variant,
                    )
                }
            }
        }
        composeRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun icon_wrap_renders_all_variants_light() {
        composeRule.setContent {
            LastroTheme(darkTheme = false) {
                IconWrapVariant.entries.forEach { variant ->
                    LastroIconWrap(
                        painter = painterResource(LastroIcons.Heart),
                        variant = variant,
                    )
                }
            }
        }
        composeRule.onRoot().assertIsDisplayed()
    }
}
