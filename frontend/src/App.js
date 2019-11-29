import React from 'react';
// import logo from './logo.svg';
import './App.css';

const App = () => {
  return (
    <div className="App">
      <header className="App-header">
        {/* <img src={logo} className="App-logo" alt="logo" /> */}
        <div>
          <p>Hi, Holly!</p>
          <p>Send some pulse surveys?</p>
          <button onClick={sendEmails}>Yes, send surveys</button>
        </div>
      </header>
    </div>
  );
}

const sendEmails = () => {
  alert('Holly is sending emails!');
 // emails will need to call backend with /happiness/#clicked in
}

export default App;
