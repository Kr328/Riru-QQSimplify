buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}