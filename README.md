# SysMon Manager (`com.tablet.sysmon`)

Custom repackaged root manager application (based on KernelSU / BakaSU) built for **HUAWEI MediaPad M5 (`CMR-W09`, Kirin 960)** running AOSP 13 with **BakaSU (`35261`) + SUSFS v2.4.0**.

## Details

- **Package ID:** `com.tablet.sysmon`
- **Native library:** `libsysmon.so`
- **Release certificate:** embedded directly into the v17 kernel (`KernelSU/kernel/manager/manager_sign.h`) or registered via `ksud kernel dynamic-manager set-apk`.
- **Prebuilt APK & matching kernel image:** available in [**`android_kernel_huawei_hi3660` Releases (`v17-sysmon`)**](https://github.com/SuspectWorkers/android_kernel_huawei_hi3660/releases/tag/v17-sysmon).

## Build

```sh
./gradlew assembleRelease
```

*(Signing keys and `local.properties` are excluded via `.gitignore`. See `sign.example.properties` for signing configuration.)*
