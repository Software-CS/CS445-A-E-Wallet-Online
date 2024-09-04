import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {Provider} from "react-redux";
import {persistor, store} from "./redux/store/store";
import list_nav from './components/element/routes';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Home from "./components/Home/Home";
import Login from "./components/auth/Login";
import {PersistGate} from "redux-persist/integration/react";
import Auth from "./components/element/Auth";
import View from "./components/Home/View";
import Register from "./components/auth/Register";
import User from "./components/admin/user/User";

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(
  <React.StrictMode>
      <Provider  store={store}>
          <PersistGate loading={null} persistor={persistor}>
              <BrowserRouter>
                  <Routes>
                      <Route path="/" element={<App/>}>
                          <Route index element={<Auth><Home /></Auth>}/>
                          {list_nav?.routes.map((route, index) => {
                              if (route.isProtected) {
                                  return (
                                      <Route
                                          key={index}
                                          path={route.path}
                                          element={<Auth>{route.element}</Auth>}
                                      />
                                  );
                              }
                          })}
                          <Route path={'/admin/users'}
                              element={<Auth>{<User/>}</Auth>}
                          />
                          {/*{list_nav?.admin_routes.map((route, index) => {*/}
                          {/*    if (route.isProtected) {*/}
                          {/*        return (*/}
                          {/*            <Route*/}
                          {/*                key={index}*/}
                          {/*                path={route.path}*/}
                          {/*                element={}*/}
                          {/*            />*/}
                          {/*        );*/}
                          {/*    }*/}
                          {/*})}*/}
                          {list_nav?.guest_routes.map((route, index) =>
                              <Route
                                  key={index}
                                  path={route.path}
                                  element={route.element}
                              />
                          )
                          }
                          <Route path="/login" element={<Login/>}/>
                          <Route path="/register" element={<Register/>}/>
                      </Route>
                  </Routes>
              </BrowserRouter>
          </PersistGate>
      </Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
