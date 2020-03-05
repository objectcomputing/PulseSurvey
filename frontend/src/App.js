import React from 'react';
// import logo from './logo.svg';
import './App.css';

const App = () => {
  return (
    <div className="App">
      <header className="App-header">
        {/* <img src={logo} className="App-logo" alt="logo" /> */}
        <div>
          <p>Hello!</p>
          <p>Would you like to send some pulse surveys?</p>
          <button onClick={sendEmails}>Yes, send surveys</button>
        </div>
      </header>
    </div>
  );
}

function handleErrors(response) {
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}

const sendEmails = async () => {
  const data = {templateName: 'emailTemplate', percentOfEmails: '22'}
  alert('Sending emails!');

 // curl -H "Content-Type: application/json" http://localhost:8080/surveys/send  -d "{"""templateName""":"""emailTemplate""","""percentOfEmails""":"""22"""}" -X POST

  const response = await fetch('http://localhost:8080/surveys/send', {
    method: 'POST',
    cache: 'no-cache',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ templateName: 'emailTemplate' , percentOfEmails: '22' })
  })
      .then(handleErrors)
      .then(response => {console.log(response)})
      .catch(error => console.log(error) )
;


  }

export default App;
