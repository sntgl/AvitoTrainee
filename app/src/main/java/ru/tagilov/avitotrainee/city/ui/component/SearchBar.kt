package ru.tagilov.avitotrainee.city.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.flow.StateFlow
import ru.tagilov.avitotrainee.R


@ExperimentalAnimationApi
@Composable
fun SearchBar(
    state: MutableState<TextFieldValue>,
    textUpdated: (String) -> Unit,
    isFocused: StateFlow<Boolean>,
    onFocusChanged: (Boolean) -> Unit,
) {
    val colorSecondary = MaterialTheme.colors.secondary
    val colorOnSecondary = MaterialTheme.colors.secondaryVariant
    val textColor = remember { mutableStateOf(colorSecondary) }
    val placeHolderDefaultText = stringResource(R.string.text_field_tip)
    val placeHolderText = remember { mutableStateOf(placeHolderDefaultText) }
    val focused = remember { isFocused }.collectAsState()
    val focusManager = LocalFocusManager.current

    SideEffect {
        if (!focused.value){
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
        AnimatedVisibility (focused.value) {
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

//@ExperimentalAnimationApi
//@Preview(showBackground = true)
//@Composable
//fun SearchBarPreview(){
//    AvitoTheme {
//        val v = remember{mutableStateOf(TextFieldValue())}
//        SearchBar(state = v, textUpdated = {}, isFocused = false)
//    }
//}