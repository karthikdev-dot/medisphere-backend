# Healthcare Risk Prediction System

A healthcare project that combines **Spring Boot, MongoDB, Python ML models, and Kafka** to manage patient data and predict health risks.

## Technologies

* Java 21
* Spring Boot
* MongoDB
* HAPI FHIR
* Python
* Flask
* TensorFlow
* Scikit-learn
* SHAP
* Apache Kafka

## What I Have Done

### Backend

* Created Spring Boot healthcare backend.
* Connected MongoDB for storing patient data.
* Added HAPI FHIR integration.
* Added Patient, Condition, Lab, Medication and Vital data.
* Added JWT security.
* Added Kafka for processing patient vitals.

### ML Models

#### Diabetes Complication Model

* Created TensorFlow model.
* Added Flask API for prediction.
* Added SHAP explainability.
* Added model version and federated round information.
* Connected the model with Spring Boot.

#### CVD Risk Model

* Trained a Random Forest model using the UCI Heart Disease dataset.
* Model accuracy: **88.52%**.
* Saved the trained model as `cvd_model.pkl`.
* Created Flask CVD prediction API.
* Connected the CVD model with Spring Boot.
* Tested the complete prediction flow successfully.

## Current Flow

```text
MongoDB
   ↓
Spring Boot
   ↓
Python Flask ML Service
   ↓
ML Model
   ↓
Prediction
   ↓
Spring Boot
```

## CVD Test Result

Example test:

```text
Result: No CVD detected
Risk: 13.5%
Risk Level: Low
```

## Current Status

* Spring Boot backend ✅
* MongoDB integration ✅
* FHIR integration ✅
* Kafka vitals processing ✅
* Diabetes complication model ✅
* SHAP explainability ✅
* CVD model ✅
* Flask ML API ✅
* Spring Boot + ML integration ✅
* Federated Learning → In progress
