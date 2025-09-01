import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import {BrowserRouter} from "react-router-dom";
import './style.css';

const rootEl = document.getElementById('app');
if (!rootEl) {
  throw new Error('Root element #app not found');
}

ReactDOM.createRoot(rootEl).render(
  <React.StrictMode>
      <BrowserRouter>
            <App />
      </BrowserRouter>
  </React.StrictMode>
);
