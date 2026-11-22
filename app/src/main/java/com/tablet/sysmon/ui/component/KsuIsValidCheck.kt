package com.tablet.sysmon.ui.component

import androidx.compose.runtime.Composable
import com.tablet.sysmon.domain.model.KernelStatus

@Composable
inline fun KsuIsValid(
    status: KernelStatus,
    content: @Composable () -> Unit,
) {
    if (status.isFullFeatured) {
        content()
    }
}
