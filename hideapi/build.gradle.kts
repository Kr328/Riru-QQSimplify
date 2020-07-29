plugins {
    id("com.android.library")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(29)

        versionCode = 10000
        versionName = "1.0.0"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

}

repositories {
    mavenCentral()
}