plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
    alias(libs.plugins.kotlin.android)
}

android {
    publishing {
        singleVariant("release"){
            withJavadocJar()
            withSourcesJar()
        }
    }
    namespace = "io.github.ftcphantom16950.phantomlib"
    compileSdk = 30

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
repositories {

}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation (libs.inspection)
    implementation (libs.blocks)
    //noinspection Aligned16KB
    implementation (libs.robotcore)
    implementation (libs.robotserver)
    implementation (libs.onbotjava)
    implementation (libs.hardware)
    implementation (libs.ftccommon)
    implementation (libs.vision)
    implementation("androidx.core:core-ktx:1.6.0")


}
afterEvaluate {
    publishing{
        publications{
            create<MavenPublication>("mavenRelease"){
                groupId = "io.github.ftcphantom16950"
                artifactId = "Actions"
                version = "1.0"

                from(components["release"])
            }
        }
    }
}