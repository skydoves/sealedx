# SealedX proguard rules #

-dontwarn com.skydoves.sealedx.**
-keep class com.skydoves.sealedx.** { *; }
-keep @com.skydoves.sealedx.core.annotations.ExtensiveSealed interface *
-keep @com.skydoves.sealedx.core.annotations.ExtensiveModel interface *