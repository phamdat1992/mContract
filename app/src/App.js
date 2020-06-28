import React, { Component } from 'react'

class App extends Component {
    constructor() {
        super()
        this.state = {
            email: '',
            password: '',
        }
    }

    handleEmail(text) {
        this.setState({ email: text.target.value })
    }

    handlePass(text) {
        this.setState({ password: text.target.value })
    }

    login() {
        console.log('test')
        console.log(this.state.email, this.state.password)

        fetch('/authenticate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=utf-8',
            },
            mode: 'cors',
            credentials: 'include',
            // referrer: 'strict-origin-when-cross-origin',
            body: JSON.stringify({
                username: this.state.email,
                password: this.state.password,
            }),
        })
            .then((response) => response.json())
            .then((data) => console.log(data))
    }

    render() {
        return (
            <div>
                <input type="text" onChange={(text) => this.handleEmail(text)} />
                <input type="text" onChange={(text) => this.handlePass(text)} />
                <button onClick={() => this.login()}>login</button>
            </div>
        )
    }
}

export default App
