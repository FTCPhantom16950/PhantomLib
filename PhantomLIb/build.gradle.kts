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
    compileSdk = 35

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation ("org.firstinspires.ftc:Inspection:11.0.0")
    implementation ("org.firstinspires.ftc:Blocks:11.0.0")
    //noinspection Aligned16KB
    implementation ("org.firstinspires.ftc:RobotCore:11.0.0")
    implementation ("org.firstinspires.ftc:RobotServer:11.0.0")
    implementation ("org.firstinspires.ftc:OnBotJava:11.0.0")
    implementation ("org.firstinspires.ftc:Hardware:11.0.0")
    implementation ("org.firstinspires.ftc:FtcCommon:11.0.0")
    implementation ("org.firstinspires.ftc:Vision:11.0.0")
    implementation(libs.core.ktx)
}
afterEvaluate {
    publishing{
        publications{
            create<MavenPublication>("mavenRelease"){
                groupId = "io.github.ftcphantom16950"
                artifactId = "PhantomLib"
                version = "1.0"

                from(components["release"])
            }
        }
    }
}