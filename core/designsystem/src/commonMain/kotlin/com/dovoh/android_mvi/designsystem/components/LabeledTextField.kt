package com.dovoh.android_mvi.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    password: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = if (password) KeyboardType.Password else KeyboardType.Text,
    onImeAction: (() -> Unit)? = null
) {
    val options = remember(imeAction, keyboardType) {
        KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType)
    }
    val actions = remember(onImeAction) {
        if (onImeAction != null) KeyboardActions(onAny = { onImeAction() })
        else KeyboardActions()
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = options,
        keyboardActions = actions,
        modifier = modifier.fillMaxWidth()
    )
}
