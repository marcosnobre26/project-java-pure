# Pure Java Machine Learning Project 🤖

Welcome to the Pure Java Machine Learning Project! This repository contains a complete, end-to-end Machine Learning pipeline implemented entirely in Java, leveraging the powerful [Weka](https://waikato.github.io/weka/index.html) library for data processing and model building.

Whether you're looking to understand the fundamentals of ML workflows or need a robust Java-based solution, this project is a great starting point! ✨

## 🚀 Features

This project demonstrates the core phases of a Machine Learning pipeline:

* ⚙️ **Configurable Pipeline:** All key parameters (data source, feature engineering steps, model type, training options) are managed via intuitive YAML configuration files.
* 📊 **Data Loading:** Efficiently loads tabular data from CSV files, handling headers, and custom delimiters.
* ✨ **Feature Engineering:** Applies essential data preprocessing techniques, including:
    * Missing value imputation.
    * One-Hot Encoding for categorical attributes.
    * Standardization for numerical features.
* 🧠 **Model Training:** Trains a classification model (default: J48 Decision Tree).
* 💾 **Model Persistence:** Saves the trained model to disk (`.ser` format) for later use or deployment.
* 📈 **Model Evaluation:** Assesses the trained model's performance on unseen test data, providing crucial metrics like Accuracy, Precision, Recall, F-Measure, and a detailed Confusion Matrix.

## 📋 Prerequisites

Before you begin, ensure you have the following installed on your system:

* **Java Development Kit (JDK) 17 or higher** ☕
* **Apache Maven** (or `mvnd` as used in the instructions for faster builds) 📦

## 🛠️ Project Setup

Follow these steps to get the project up and running:

1.  **Clone or Download:**
    * If you're using Git: `git clone <repository_url_here>`
    * Otherwise, download the project ZIP and extract it.
2.  **Navigate to Project Root:**
    Open your terminal/command prompt and navigate to the root directory of the project (e.g., `cd eclipse-workspace/project-java-pure`).
3.  **Create Configuration Files:**
    The project relies on YAML configuration files located in `src/main/resources/config/`. Ensure these files exist with the following content:

    * **`src/main/resources/config/data_config.yml`**
        This file defines how your raw data is loaded.
        ```yaml
        # Data Configuration
        targetColumn: income # The name of your target/label column in the CSV
        sourceType: CSV      # The type of data source (e.g., CSV, DATABASE)

        csvConfig:
          # IMPORTANT: Use the ABSOLUTE PATH to your adult_train.csv file.
          # Replace 'C:/Users/marco/eclipse-workspace/project-java-pure' with your actual project root path.
          filePath: C:/Users/marco/eclipse-workspace/project-java-pure/src/main/resources/data/adult_train.csv
          hasHeader: true # Does your CSV have a header row?
          separator: ';'  # The delimiter used in your CSV (e.g., ',', ';', '\t')
        ```
        *Remember to update the `filePath` to your specific absolute path!*

    * **`src/main/resources/config/feature_config.yml`**
        This file configures feature engineering steps. In this current implementation, the core preprocessing filters are hardcoded in `FeatureExtractor.java` for simplicity, but this file is a placeholder for future dynamic configuration.
        ```yaml
        # Feature Engineering Configuration
        # (Currently, specific filter parameters are applied in FeatureExtractor.java)
        # This file can be extended to dynamically configure filters or feature selection in the future.
        ```

    * **`src/main/resources/config/training_config.yml`**
        This file defines your chosen ML model and its training parameters.
        ```yaml
        # Model Training Configuration
        modelType: J48 # The Weka classifier to use (e.g., J48, RandomForest, NaiveBayes)
        modelOptions: ["-C", "0.25", "-M", "2"] # Command-line options for the Weka classifier
        modelOutputPath: target/model/weka-j48-model.ser # Where the trained model will be saved
        ```

    * **`src/main/resources/config/model_config.yml`**
        This file provides general model configurations.
        ```yaml
        # Model Configuration
        # This file can be used for specific model loading or deployment settings.
        # Currently, the training output path is primarily defined in training_config.yml.
        modelOutputPath: target/model/weka-model.ser # Example path (can be synchronized with training_config)
        ```

4.  **Place Dataset Files:**
    Your raw data files (`adult_train.csv` and `adult_test.csv`) should be placed in `src/main/resources/data/`.
    * **Create the `data` folder:** If it doesn't exist, create it inside `src/main/resources/`.
    * **`src/main/resources/data/adult_train.csv`**
        This is your training dataset.
        ```csv
        age;workclass;fnlwgt;education;education_num;marital_status;occupation;relationship;race;sex;capital_gain;capital_loss;hours_per_week;native_country;income
        39;State-gov;77516;Bachelors;13;Never-married;Adm-clerical;Not-in-family;White;Male;2174;0;40;United-States;<=50K
        50;Self-emp-not-inc;83311;Bachelors;13;Married-civ-spouse;Exec-managerial;Husband;White;Male;0;0;13;United-States;<=50K
        38;Private;215646;HS-grad;9;Divorced;Handlers-cleaners;Not-in-family;White;Male;0;0;40;United-States;<=50K
        53;Private;234721;11th;7;Married-civ-spouse;Handlers-cleaners;Husband;Black;Male;0;0;40;United-States;<=50K
        28;Private;338409;Bachelors;13;Married-civ-spouse;Prof-specialty;Wife;Black;Female;0;0;40;Cuba;<=50K
        37;Private;284582;Masters;14;Married-civ-spouse;Exec-managerial;Wife;White;Female;0;0;40;United-States;<=50K
        49;Private;160187;9th;5;Married-civ-spouse;Other-service;Husband;Black;Male;0;0;16;Jamaica;<=50K
        52;Self-emp-not-inc;209642;HS-grad;9;Married-civ-spouse;Exec-managerial;Husband;White;Male;0;0;45;United-States;>50K
        ```

    * **`src/main/resources/data/adult_test.csv`**
        This is your test dataset, crucial for evaluating the model on unseen data.
        ```csv
        age;workclass;fnlwgt;education;education_num;marital_status;occupation;relationship;race;sex;capital_gain;capital_loss;hours_per_week;native_country;income
        40;Private;120000;HS-grad;9;Married-civ-spouse;Craft-repair;Husband;White;Male;0;0;40;United-States;>50K
        30;Self-emp-inc;150000;Masters;14;Never-married;Prof-specialty;Not-in-family;White;Female;0;0;50;United-States;>50K
        22;Private;180000;Some-college;10;Never-married;Sales;Own-child;White;Male;0;0;20;United-States;<=50K
        60;Private;200000;Bachelors;13;Married-civ-spouse;Exec-managerial;Husband;White;Male;0;0;60;United-States;>50K
        29;Local-gov;250000;HS-grad;9;Never-married;Tech-support;Not-in-family;White;Female;0;0;35;Canada;<=50K
        ```
5.  **Review `MLApplication.java`:**
    Ensure your `src/main/java/com/machinelearning/purejava/main/MLApplication.java` file is up-to-date with the latest code, especially the model evaluation section.

## 🏃 How to Run

Once you have set up all the configuration and data files, execute the project using Maven:

```bash
mvnd clean install exec:java