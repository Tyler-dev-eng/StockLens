# Stock Market Data App
## Overview
This Android application allows users to retrieve and display real-time stock market data using a free API. It showcases key information about stocks, including their symbol, name, exchange, and intraday data, and visualizes this data through a chart. The app is designed to demonstrate the implementation of various software engineering principles, including:
1. **Room Database** for persistent local storage.
2. **SOLID Principles** for better code structure and maintainability.
3. **Clean Architecture** for modular, testable, and scalable code.

This project is part of my learning journey aimed at improving my Android development skills, specifically in building apps that handle data fetching, persistence, and UI updates efficiently.

## Features
1. **Stock Listings**: Fetches and displays a list of company listings, allowing users to search for stocks.
2. **Intraday Stock Info**: Retrieves detailed intraday stock data and displays it.
3. **Chart Visualization**: Displays stock data on a chart for visual analysis.
4. **Offline Persistence**: Uses Room Database to cache stock data for offline access.

## Architecture
The app follows **Clean Architecture** and implements **SOLID principles**, making it easier to maintain, test, and scale. Here’s a high-level breakdown:
1. **Domain Layer**: Contains business logic and model representations.
2. **Data Layer**: Handles data retrieval from APIs and persistence in the Room database.
3. **Presentation Layer**: Manages UI components and user interactions.

## API Setup
This app uses a free stock market API (from https://www.alphavantage.co/)  to retrieve data. The API endpoint details are defined within the app.

## Tutorial Source
This app was built following a tutorial by Philipp Lackner (https://www.youtube.com/watch?v=uLs2FxFSWU4&ab_channel=PhilippLackner). You can follow the tutorial to learn more about building similar apps with Android.

## Screenshots
<div style="display: flex; justify-content: space-evenly; gap: 20px;">
  <img src="https://github.com/Tyler-dev-eng/StockLens/blob/main/Screenshot_20250128_132112.png" width="250" height="550" />
  <img src="https://github.com/Tyler-dev-eng/StockLens/blob/main/Screenshot_20250128_132238.png" width="250" height="550" />
  <img src="https://github.com/Tyler-dev-eng/StockLens/blob/main/Screenshot_20250128_132305.png" width="250" height="550" />
</div>
