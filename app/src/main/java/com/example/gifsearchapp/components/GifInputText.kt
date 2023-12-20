package com.example.gifsearchapp.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GifInputText(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    maxLine: Int = 1,
    onTextChange: (String) -> Unit,
    onImeAction: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textFieldFocusRequester = remember { FocusRequester() }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedLabelColor = Color.DarkGray,
            unfocusedLabelColor = Color.LightGray,
            cursorColor = Color.DarkGray,
            selectionColors = TextSelectionColors(
                handleColor = Color.DarkGray,
                backgroundColor = Color.DarkGray
            ),
            unfocusedIndicatorColor = Color.LightGray,
            focusedIndicatorColor = Color.DarkGray
        ),
        maxLines = maxLine,
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
                keyboardController?.hide()
                focusManager.clearFocus()
            }),

        modifier = modifier
            .focusRequester(textFieldFocusRequester)
            .onFocusChanged {
                isTextFieldFocused = it.isFocused
            },
    )
    BackHandler(enabled = isTextFieldFocused) {
        keyboardController?.hide()
        focusManager.clearFocus(force = true)
    }
}