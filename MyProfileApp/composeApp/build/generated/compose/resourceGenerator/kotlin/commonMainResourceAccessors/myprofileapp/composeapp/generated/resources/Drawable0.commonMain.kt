@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package myprofileapp.composeapp.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi

private object CommonMainDrawable0 {
  public val profile: DrawableResource by 
      lazy { init_profile() }
}

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("profile", CommonMainDrawable0.profile)
}

internal val Res.drawable.profile: DrawableResource
  get() = CommonMainDrawable0.profile

private fun init_profile(): DrawableResource = org.jetbrains.compose.resources.DrawableResource(
  "drawable:profile",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/myprofileapp.composeapp.generated.resources/drawable/profile.webp", -1, -1),
    )
)
