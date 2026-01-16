# File upload

File upload components allow users to select, upload, and preview files such as images or documents.

## Components

- `FileUploadButton`: High-level component for multiple file selection with a button trigger and optional file previews. This is the default component for file uploads.
- `FileUploadSingleButton`: High-level component for single file selection with a button trigger and optional file preview.
- `FileUploadPattern`: Reusable pattern wrapper that can integrate file upload functionality into any component.
- `FileUploadDefaultPreview`: Default preview implementation that displays uploaded files in a list with progress bars, error states, and clear buttons.
- `PreviewFile`: Visual representation of a single uploaded file, showing icon, name, size and a clear button.

## Pattern API

The file upload functionality is built on a reusable pattern that can be integrated into any component. The pattern handles all file picking logic while allowing you to customize the UI trigger and preview display.

### Basic Usage

```kotlin
// Single file upload
var selectedFile by remember { mutableStateOf<UploadedFile?>(null) }

FileUploadSingleButton(
    onResult = { file -> selectedFile = file },
    label = "Select file",
)

// Multiple file upload with max limit
var selectedFiles by remember { mutableStateOf<ImmutableList<UploadedFile>>(persistentListOf()) }

FileUploadButton(
    onResult = { files -> selectedFiles = files },
    label = "Select files",
    maxFiles = 5, // Optional: limit to 5 files
)
```

### Using the Pattern with Custom Components

The `FileUploadPattern` can wrap any component to add file upload functionality:

```kotlin
val pattern = rememberFileUploadPattern(
    type = FileUploadType.Image(ImageSource.Both),
    mode = FileUploadMode.Single,
    onFilesSelected = { files -> /* handle files */ }
)

// With a Button
FileUploadPattern(pattern = pattern) { onClick ->
    ButtonFilled(
        onClick = onClick,
        text = "Upload Image"
    )
}

// With a Surface
FileUploadPattern(pattern = pattern) { onClick ->
    Surface(
        onClick = onClick,
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Tap to upload")
    }
}

// With direct launcher access (e.g., in a TextField trailing icon)
TextField(
    trailingIcon = {
        IconButton(onClick = { pattern.launchPicker() }) {
            Icon(SparkIcons.Upload)
        }
    }
)

## Default Preview

The `FileUploadDefaultPreview` component provides a standard implementation for displaying uploaded files:

```kotlin
FileUploadButton(
    onResult = { files -> selectedFiles = files },
    label = "Select files",
)

FileUploadDefaultPreview(
    files = selectedFiles,
    onClearFile = { file -> selectedFiles = selectedFiles.remove(file) },
    onClick = { file -> /* handle file click */ }
)
```

The default preview automatically:
- Shows each file with its icon, name, and size
- Displays progress bars during upload
- Shows error messages with appropriate styling
- Provides clear buttons for removing files
- Supports clickable previews for opening files

## File Types

You can specify what type of files to select:

```kotlin
// Images only (with camera/gallery selection)
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select image",
    type = FileUploadType.Image(
        source = ImageSource.Both // Shows dialog to choose camera or gallery
    )
)

// Images only (camera only)
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Take photo",
    type = FileUploadType.Image(source = ImageSource.Camera)
)

// Images only (gallery only)
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Choose from gallery",
    type = FileUploadType.Image(source = ImageSource.Gallery)
)

// Videos only
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select video",
    type = FileUploadType.Video
)

// Images and videos
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select media",
    type = FileUploadType.ImageAndVideo
)

// Generic files with extension filter
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Select PDF",
    type = FileUploadType.File(extensions = setOf("pdf"))
)
```

## Custom Preview

You can customize how selected files are displayed:

```kotlin
FileUploadSingleButton(
    onResult = { /* handle */ },
    label = "Upload",
    preview = { file ->
        file?.let {
            // Custom preview layout
            Row {
                Icon(SparkIcons.Check)
                Text(it.name)
            }
        }
    }
)

FileUploadButton(
    onResult = { /* handle */ },
    label = "Upload multiple",
    preview = { files ->
        // Custom preview for multiple files
        files.forEach { file ->
            PreviewFile(
                file = file,
                onClear = { /* remove file */ }
            )
        }
    }
)
```

## Multiple File Selection

For multiple file selection, you can optionally limit the maximum number of files:

```kotlin
FileUploadButton(
    onResult = { files -> /* handle */ },
    label = "Select up to 5 files",
    maxFiles = 5, // Limit to 5 files
)
```

## Opening Files

You can open files with the system's default application using `FileUploadDefaults.openFile()`:

```kotlin
PreviewFile(
    file = uploadedFile,
    onClear = { /* remove file */ },
    onClick = {
        // Open file with default application
        FileUploadDefaults.openFile(uploadedFile)
    }
)
```

Note: On Android, if opening files from custom locations (e.g., `FileKit.filesDir` or `FileKit.cacheDir`), you may need to configure FileProvider in your app's AndroidManifest.xml. See [FileKit documentation](https://filekit.mintlify.app/dialogs/open-file) for details.

## PreviewFile Customization

The `PreviewFile` component supports several customization options:

```kotlin
PreviewFile(
    file = uploadedFile,
    onClear = { /* remove file */ },
    modifier = Modifier, // Optional: modifier for the component
    progress = { 0.5f }, // Optional: determinate progress (0.0 to 1.0)
    isLoading = false, // Optional: indeterminate loading state
    errorMessage = null, // Optional: error message to display
    enabled = true, // Optional: whether the component is enabled
    clearContentDescription = "Remove ${uploadedFile.name}", // Optional: accessibility label
    onClick = { /* handle file click */ }, // Optional: makes the preview clickable
    clearIcon = SparkIcons.Close, // Optional: customize the clear button icon
)
```

### Loading States

The `PreviewFile` component supports two types of loading indicators:

1. **Determinate Progress**: Use the `progress` parameter to show a progress bar with a specific value (0.0 to 1.0)
2. **Indeterminate Loading**: Use the `isLoading` parameter to show an indeterminate progress bar when the exact progress is unknown

When either `progress` or `isLoading` is active, the clear button is automatically disabled.

## Button Semantics

You can add accessibility labels to the file upload buttons:

```kotlin
FileUploadButton(
    onResult = { files -> /* handle */ },
    label = "Select files",
    onClickLabel = "Open file picker to select multiple files", // Optional: accessibility label
)

FileUploadSingleButton(
    onResult = { file -> /* handle */ },
    label = "Select file",
    onClickLabel = "Open file picker to select a single file", // Optional: accessibility label
)
```

## Low-Level Pattern API

For advanced use cases, you can use the pattern state directly:

```kotlin
val pattern = rememberFileUploadPattern(
    onFilesSelect = { files -> /* handle selected files */ },
    type = FileUploadType.File(),
    mode = FileUploadMode.Multiple(maxFiles = 10),
    title = "Select files", // Optional: dialog title
    directory = null, // Optional: initial directory
    dialogSettings = FileKitDialogSettings.createDefault(), // Optional: dialog settings
)

// Launch specific pickers
pattern.launchFilePicker()      // File/gallery picker
pattern.launchCameraPicker()   // Camera picker
pattern.launchGalleryPicker()  // Gallery picker
pattern.launchPicker()          // Smart launcher (handles source selection)

// Query pattern configuration
val isSingleMode = pattern.isSingleMode
val maxFiles = pattern.maxFiles
```

