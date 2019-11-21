import React from 'react';
import logo from './logo.svg';
import './App.css';

const App = () => {
  return (
    <div className="App">
      <header className="App-header">
        {/* <img src={logo} className="App-logo" alt="logo" /> */}
        <div>
          <p>Send some pulse surveys?</p>
          <button onClick={sendEmails}>Yes, send surveys</button>
        </div>
      </header>
    </div>
  );
}

const sendEmails = () => {
  alert('I\'m sending emails!');
 // emails will need to call backend with /happiness/#clicked in
}

export default App;
