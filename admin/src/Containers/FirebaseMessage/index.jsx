import * as Axios from 'axios';
import React from 'react';
import { toast } from 'react-toast';
import { Container, Spinner, FormControl, InputGroup, Button } from 'react-bootstrap';
import { Formik } from 'formik';
import { onMessageListener } from '@Utils/firebaseInit';

Axios.defaults.baseURL = 'https://mcontract.vn/api';

export const FirebaseMessage = () => {
  const [messages, setMessages] = React.useState([]);
  const [requesting, setRequesting] = React.useState(false);

  React.useEffect(() => {
    // setRequesting(true);
  }, []);

  onMessageListener()
    .then(payload => {
    })
    .catch(err => {
      console.error(err);
    });

  return (
    <Container>
      <Formik
        initialValues={{
          name: '',
          message: '',
        }}
        onSubmit={(values, actions) => {
          Axios.get('/otps/test')
            .then(resp => {
              // setMessages(resp.data.messages.concat(messages));
              // actions.setSubmitting(false);
              toast.success('Submitted succesfully');
            })
            .catch(err => {
              toast.error('There was an error saving the message');
            });
        }}
      >
        {prop => {
          const { handleSubmit, handleChange, isSubmitting } = prop;
          return (
            <>
              <InputGroup className="mb-3">
                <InputGroup.Prepend>
                  <InputGroup.Text id="basic-addon1">Name</InputGroup.Text>
                </InputGroup.Prepend>
                <FormControl placeholder="Enter your name" onChange={handleChange('name')} />
              </InputGroup>
              <InputGroup className="mb-3">
                <InputGroup.Prepend>
                  <InputGroup.Text id="basic-addon1">Message</InputGroup.Text>
                </InputGroup.Prepend>
                <FormControl onChange={handleChange('message')} placeholder="Enter a message" />
              </InputGroup>
              {isSubmitting ? (
                <Button variant="primary" disabled>
                  <Spinner as="span" size="sm" role="status" animation="grow" aria-hidden="true" />
                  Loading...
                </Button>
              ) : (
                <Button variant="primary" onClick={() => handleSubmit()}>
                  Submit
                </Button>
              )}
            </>
          );
        }}
      </Formik>
      <div className="message-list">
        <h3>Messages</h3>
        {requesting ? (
          <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
          </Spinner>
        ) : (
          <>
            {/* {messages.map((m, index) => {
              const { name, message } = m;
              return (
                <div key={index}>
                  {name}: {message}
                </div>
              );
            })} */}
          </>
        )}
      </div>
    </Container>
  );
};
