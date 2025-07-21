# Java Machine Learning API 🤖

Welcome to the Java Machine Learning API project! This repository showcases a complete, end-to-end Machine Learning system built entirely in Java. It demonstrates how to train a predictive model, save it, and then serve it through a robust RESTful API built with **Spring Boot**.

This project serves as a comprehensive blueprint for operationalizing a Machine Learning model, covering everything from data preprocessing to real-time prediction.

## ✨ Features

* **RESTful API for Predictions:** 🌐 Exposes the trained model through a clean, versioned REST API (`/api/v1/predict`) to serve real-time predictions.
* **Rich JSON Responses:** 📝 Provides detailed, informative JSON responses, including prediction probabilities, a unique prediction ID, and a timestamp.
* **Separated Training & Serving:** ↔️ A clear distinction between the model training script (`ModelTrainerApp`) and the API server (`MLApplication`), which is a best practice for MLOps.
* **Configurable Pipeline:** ⚙️ Key parameters for data loading and model training are managed via intuitive YAML configuration files.
* **Robust Feature Engineering:** ✨ Utilizes the powerful **Weka** library to apply essential preprocessing techniques:
    * Missing value imputation.
    * One-Hot Encoding for categorical features.
    * Standardization (Z-score normalization) for numerical features.
* **Model Persistence:** 💾 Saves the trained model (a J48 Decision Tree) and the "fit" `FeatureExtractor` to disk as `.ser` files, ensuring the exact same data transformations are applied in both training and prediction.

## 🛠️ Technologies Used

This project leverages a modern and robust stack of Java technologies:

* **Core Framework:** [**Spring Boot**](https://spring.io/projects/spring-boot) (v2.7.14) - For creating a stand-alone, production-grade REST API with a built-in Tomcat server.
* **Machine Learning:** [**Weka**](https://waikato.github.io/weka/index.html) (v3.8.6) - A comprehensive library for data mining tasks, including preprocessing, classification, and modeling.
* **Build & Dependency Management:** [**Apache Maven**](https://maven.apache.org/) - For managing project dependencies and the build lifecycle.
* **Data Handling:**
    * [**Jackson**](https://github.com/FasterXML/jackson) - For seamless serialization and deserialization between Java objects and JSON.
    * [**SnakeYAML**](https://bitbucket.org/snakeyaml/snakeyaml/src/master/) - For parsing the `.yml` configuration files.
* **Programming Language:** [**Java 17**](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ☕

## 🧠 How It Works: From Training to Prediction

The system is designed in two distinct phases, which is a standard pattern in Machine Learning operations.

### Phase 1: The Training Phase (Offline) 🏋️‍♂️

This phase is executed once by running the `ModelTrainerApp.java` class. Its goal is to learn patterns from historical data and create two essential artifacts.

1.  **The Raw Material (The CSV):** The process starts with `adult_full_train.csv`, a dataset with over 32,000 examples of individuals, each described by 14 features (like `age`, `education`, `workclass`) and the target variable we want to predict: `income`.
2.  **The Learning Algorithm (J48 Decision Tree):** We use the J48 algorithm from Weka. Think of it as a highly logical detective. It analyzes all 32,000 examples to find the most effective questions to ask to separate people who earn `>50K` from those who earn `<=50K`. It learns, for instance, that `marital_status` is a very powerful initial question.
3.  **The Learned Pattern (The Decision Tree):** The final output of the J48 algorithm is a massive **Decision Tree**. This tree is the "pattern" the model has learned. It's a complex set of "if-then-else" rules that guide the classification of any new individual.
4.  **The Saved Artifacts:**
    * **`weka-j48-model.ser`:** This is a serialized Java object—a "snapshot" of the fully trained decision tree, containing all its rules and logic.
    * **`feature-extractor.ser`:** Raw data is messy. Text like "Private" must be converted into numbers for the model. The `FeatureExtractor` learns *how* to perform these transformations (e.g., it learns the average `age` to fill in missing values, it learns all possible `workclass` categories for one-hot encoding). This "instruction manual" is saved so we can apply the *exact same transformations* to new data arriving at our API.

### Phase 2: The Prediction Phase (Real-Time API) ⚡

This is what happens every time a request is sent to the running API (e.g., from Postman).

1.  **The Request:** A client sends a `POST` request to `http://localhost:8080/api/v1/predict` with a JSON body containing the features of a new individual.
2.  **The Entrypoint (`PredictionController`):** Spring Boot receives the request and, based on the URL and `POST` method, routes it to our `PredictionController`.
3.  **The Core Logic (`PredictionService`):**
    * The service first uses the loaded `feature-extractor.ser` to transform the incoming JSON data into a Weka-compatible format (`Instance`). This step ensures the new data is processed identically to the training data.
    * This clean, processed `Instance` is then passed to the loaded `weka-j48-model.ser`.
    * The model follows the branches of its decision tree based on the new data's features.
    * It arrives at a "leaf" node, which contains the probabilities for that specific profile (e.g., "For people like this, 95% earn `<=50K` and 5% earn `>50K`").
    * The service takes this result and constructs a rich `PredictionResponse` object.
4.  **The Response:** The `PredictionResponse` object is converted back into a JSON string by Spring Boot and sent back to the client, completing the request.

## ⚙️ Project Setup & How to Run

### ✅ Prerequisites

* **Java Development Kit (JDK) 17 or higher**
* **Apache Maven**

### 👉 Instructions

#### 1. Train the Model (Run this once)

First, you need to generate the model files.

1.  **Prepare the Dataset:** Ensure the `adult_full_train.csv` file is located in `src/main/resources/data/`.
2.  **Run the Trainer:** In your IDE, find the `src/main/java/com/machinelearning/purejava/main/ModelTrainerApp.java` file. Right-click it and run it as a Java Application.
3.  **Verify the Artifacts:** After the process completes, check that the following two files have been created in `src/main/resources/model/`:
    * `weka-j48-model.ser`
    * `feature-extractor.ser`

#### 2. Run the API Server 🚀

Now, with the trained artifacts in place, you can start the web server.

1.  **Run the Application:** In your IDE, find the `src/main/java/com/machinelearning/purejava/main/MLApplication.java` file. Right-click it and run it as a **Spring Boot App**.
2.  **Check the Console:** The console will show the Spring Boot banner, and the last lines should indicate that the Tomcat server has started on port 8080.

#### 3. Test the API 🧪

Use an API client like [Postman](https://www.postman.com/) or `curl` to test the endpoint.

* **Method:** `POST`
* **URL:** `http://localhost:8080/api/v1/predict`
* **Body:** `raw` -> `JSON`
* **JSON Payload:**
    ```json
    {
        "features": {
            "age": 39,
            "workclass": "State-gov",
            "education": "Bachelors",
            "marital_status": "Never-married",
            "occupation": "Adm-clerical",
            "relationship": "Not-in-family",
            "race": "White",
            "sex": "Male",
            "capital_gain": 2174,
            "capital_loss": 0,
            "hours_per_week": 40,
            "native_country": "United-States"
        }
    }
    ```

You will receive a detailed JSON response with the prediction and associated probabilities.