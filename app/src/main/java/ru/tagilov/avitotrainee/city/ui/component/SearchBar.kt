package ru.tagilov.avitotrainee.city.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.tagilov.avitotrainee.R


@Composable
fun SearchBar(
    state: MutableState<TextFieldValue>,
    textUpdated: (String) -> Unit,
    focused: Boolean,
    onFocusChanged: (Boolean) -> Unit,
) {
    val colorSecondary = MaterialTheme.colors.secondary
    val colorOnSecondary = MaterialTheme.colors.secondaryVariant
    val textColor = remember { mutableStateOf(colorSecondary) }
    val placeHolderDefaultText = stringResource(R.string.text_field_tip)
    val placeHolderText = remember { mutableStateOf(placeHolderDefaultText) }
    val focusManager = LocalFocusManager.current

    SideEffect {
        if (!focused){
            focusManager.clearFocus()
            state.value = TextFieldValue("")
            textUpdated("")
            textColor.value = colorOnSecondary
            placeHolderText.value = placeHolderDefaultText
        } else {
            textColor.value = colorSecondary
            placeHolderText.value = placeHolderDefaultText
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(MaterialTheme.colors.background)
    ) {
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
                textUpdated(value.text)
            },
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
                .onFocusChanged {
                    onFocusChanged(it.isFocused)
                },
            textStyle = MaterialTheme.typography.body1,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "",
                    modifier = Modifier
                        .size(16.dp)
                )
            },
            trailingIcon = null,
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.secondary,
                cursorColor = MaterialTheme.colors.primary,
                leadingIconColor = textColor.value,
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            placeholder = {
                Text(
                    placeHolderText.value,
                    color = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier.fillMaxWidth())
                },
        )
        AnimatedVisibility (focused) {
            Text(
                text = stringResource(id = R.string.cancel),
                modifier = Modifier
                    .clickable { onFocusChanged(false) }
                    .fillMaxHeight()
                    .padding(end = 28.dp, start = 12.dp)
                    .wrapContentHeight(),
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}
