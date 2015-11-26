# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/gkudva/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-dontobfuscate
-dontoptimize
-repackageclasses ''

#Jackson
-dontwarn com.fasterxml.jackson.databind.**

#View Pager Indicator
-dontwarn com.viewpagerindicator.**

#Android
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class android.support.v13.app.** { *; }
-keep interface android.support.v13.app.** { *; }

#droid4me
-keep class com.smartnsoft.** { *; }

#my app
-keep class my.app.package.** { *; }

#Critercism
-keep public class com.crittercism.**
-keepclassmembers public class com.crittercism.* { *; }
-keep class android.support.v7.widget.LinearLayoutManager { *; }