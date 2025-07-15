# ASCII Art Generator (Java)

A modular Java application that converts image files into ASCII art representations.  
The program supports both console and HTML output formats and includes an interactive command-line interface (CLI) for user control.

---

## 🧩 Features

- Converts grayscale images to ASCII art using brightness matching
- Outputs to terminal or HTML (`html.out`)
- Adjustable resolution (zoom in/out)
- Custom character set management (add/remove single chars or ranges)
- CLI interface with menu-style interaction
- Efficient image processing using iterators and optimized data structures

---

## 📦 Project Structure

The source code is organized into modular Java packages:
main/
├── README.md
├── image/
│ ├── Image.java # Interface for image operations
│ ├── FileImage.java # Loads and pads images; handles sub-image logic
│ ├── ImageIterableProperty.java# Iterable support for image traversal
│ └── package-info.java
├── ascii_output/
│ ├── AsciiOutput.java # Output interface
│ ├── ConsoleAsciiOutput.java # ASCII output to console
│ ├── HtmlAsciiOutput.java # ASCII output to HTML file
│ └── package-info.java
├── ascii_art/
│ ├── Shell.java # Command-line interface (CLI)
│ ├── Driver.java # Program launcher
│ ├── package-info.java
│ └── img_to_char/
│ ├── BrightnessImgCharMatcher.java # Maps image sections to ASCII chars
│ ├── CharRenderer.java # Converts char to pixel matrix
│ └── package-info.java


Each package encapsulates a separate concern:
- `image/` – image loading, padding, and sub-image logic
- `ascii_output/` – rendering output to different formats
- `ascii_art/` – user interaction and main program control
- `ascii_art.img_to_char/` – brightness-based character matching logic

---

## ⏱ Complexity & Performance

### Sub-image creation
- **Time:** `O(n)` where `n` = number of pixels. Padding: `O(log n)`
- **Memory:** `O(2n)` due to duplication (original + sub-images)
- `ArrayList` was chosen for ordered traversal and iterator compatibility

### ASCII conversion
- **Time:**
  - Brightness calculation of all characters (once): `O(m)` (constant)
  - Brightness of each sub-image: `O(n)`
  - Matching each sub-image to closest brightness char: `O(n)`
- **Memory:**
  - Used `HashMap` to store char brightness for fast `O(1)` lookup

---

## 🔄 Iterator Use

Sub-images are stored in an `ArrayList` and accessed using Java’s built-in iterator.  
Chosen because:
- It preserves order
- Allows safe element removal during iteration
- Matches Java’s standard idioms

---

## 🚀 Example Usage

```java
Image img = Image.fromFile("path/to/image.jpg");
BrightnessImgCharMatcher matcher = new BrightnessImgCharMatcher(img, "Courier New");
char[][] ascii = matcher.chooseChars(64, new Character[]{'@', '#', '.', ' '});
