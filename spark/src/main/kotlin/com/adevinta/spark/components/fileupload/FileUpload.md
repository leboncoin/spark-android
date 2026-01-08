# File upload

File upload components allow users to select, upload, and preview files such as images or documents.

## Components

- `FileUploadButton`: High-level component combining a button to trigger file selection and an optional list of file previews.
- `PreviewFile`: Visual representation of a single uploaded file, showing icon, name, size and a clear button.

## Usage

```kotlin
val files by remember { mutableStateOf(emptyList<UploadedFile>()) }

Column {
    FileUploadButton(
        files = files,
        onClick = { /* trigger file picker and update files */ },
        onClearFile = { file -> files = files.filterNot { it.id == file.id } },
        label = "Select file",
        multiple = true,
    )
}
```

