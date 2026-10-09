plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("com.android.lint")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "com.example.shared"
        compileSdk = 34
        minSdk = 24

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "sharedKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:2.2.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
                implementation("io.ktor:ktor-client-core:2.3.12")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
            }
        }

        commonTest {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test:2.2.0")
            }
        }

        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:2.3.12")
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation("androidx.test.ext:junit:1.3.0")
                implementation("androidx.test:core:1.5.0")
                implementation("androidx.test:runner:1.5.2")
            }
        }

        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:2.3.12")
            }
        }
    }

}