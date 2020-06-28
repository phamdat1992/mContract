import React from 'react'
import './MainPage.css'

class MainPage extends React.Component {
    render() {
        return (
            <div class="login-div">
                <div class="logo"></div>
                <div class="title">Red Stapler</div>
                <div class="sub-title">BETA</div>
                <div class="fields">
                    <div class="username">
                        <input type="username" class="user-input" placeholder="username" />
                    </div>
                    <div class="password">
                        <input type="password" class="pass-input" placeholder="password" />
                    </div>
                </div>
                <button class="signin-button">Login</button>
                <div class="link">
                    <a href="#">Forgot password?</a> or <a href="#">Sign up</a>
                </div>
            </div>
        )
    }
}

export default MainPage
