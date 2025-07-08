# Cat vs Dog Classifier Android App

An Android application that classifies images of cats and dogs using a Convolutional Neural Network (CNN) model optimized with TensorFlow Lite.

---

## Overview

This app leverages deep learning to identify whether an image contains a cat or a dog. The model was trained on a dataset of 4,000 images (2,000 cats and 2,000 dogs) using TensorFlow with a CNN architecture. The trained model was then converted to TensorFlow Lite format to enable efficient inference directly on Android devices.

---

## Features

- **Accurate Classification:** Uses a CNN trained on a balanced dataset for robust cat and dog classification.
- **On-device Inference:** Utilizes TensorFlow Lite for fast and lightweight model inference without requiring a server.
- **User-friendly Interface:** Simple Android Java app to upload or capture images and get instant classification results.
- **Offline Functionality:** Does not rely on an internet connection to perform classification.

---

## Screenshots

*Include screenshots of your app here (optional but recommended)*

---

## Technologies Used

- **Android Studio** (Java)
- **TensorFlow** (CNN model training)
- **TensorFlow Lite** (model conversion and inference)
- **Camera / Gallery access** in Android

---

## Getting Started

### Prerequisites

- Android Studio installed
- Android device or emulator running Android 5.0 (API level 21) or above

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Nahidhasan24/CatDogPrediction.git
