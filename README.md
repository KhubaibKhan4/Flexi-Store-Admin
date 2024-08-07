# Flexi-Store-Admin
Flexi-Store-Admin is a Seller Central for the Flexi-Store-KMP Project that Supports Android, iOS, Web & Desktop. By using Flexi Store Admin, We can add New Products, Edit Exisitng Products, Add New Categories, Edit Existing Categories, Add Promotion Cards, Enable and Disable them, Display the Promotion Cards up to Specific Date as well.

## Desktop Screen Shots

| ![Screenshot 1](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/1.png) | 
| --- |
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/2.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/3.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/4.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/5.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/6.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/7.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/8.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/9.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/10.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/11.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/12.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/13.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/14.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/15.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/16.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/17.png) | 
| ![Screenshot 2](https://github.com/KhubaibKhan4/Flexi-Store-Admin/blob/master/assests/screenshots/18.png) | 

## Future Plans
- Payment Integration.
- PayPal Integration.

## 🌟 Contributions
If you wanna contribute, Please make sure to add new features & Then make a PR.Feel free to contribute to the project and stay tuned for more exciting updates!

# Open To Work
Do you wanna Convert your thoughts into Physicall & Successfull Project Then I'm here for you. I'm open to work, available for Freelance or Remote Work Opportunities. Feel free to reach me out on Email.

## 🤝 Connect with Me

Let's chat about potential projects, job opportunities, or any other collaboration! Feel free to connect with me through the following channels:

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/khubaibkhandev)
[![Twitter](https://img.shields.io/badge/Twitter-Follow-blue?style=for-the-badge&logo=twitter)](https://twitter.com/codespacepro)
[![Email](https://img.shields.io/badge/Email-Drop%20a%20Message-red?style=for-the-badge&logo=gmail)](mailto:18.bscs.803@gmail.com)

  ## 💰 You can help me by Donating
  [![BuyMeACoffee](https://img.shields.io/badge/Buy%20Me%20a%20Coffee-ffdd00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black)](https://buymeacoffee.com/khubaibkhan) [![PayPal](https://img.shields.io/badge/PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://paypal.me/18.bscs) [![Patreon](https://img.shields.io/badge/Patreon-F96854?style=for-the-badge&logo=patreon&logoColor=white)](https://patreon.com/MuhammadKhubaibImtiaz) [![Ko-Fi](https://img.shields.io/badge/Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/muhammadkhubaibimtiaz) 

## Setup
To use this Admin Panel, You need to setup the Flexi-Store Server first. To Setup the Server, you need to follow the Server Guide. [Flexi-Store-Server](https://github.com/KhubaibKhan4/Flexi-Store-Server).
After setting up the server, You need to get the `local ip` from your terminal using `Command Prompt` or  `Terminal`. You just need to replace your ip with `BASE_URL` inside the `composeApp/src/commonMain/kotlin/utils/Constant.kt`

This is a Kotlin Multiplatform project targeting Web, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

**Note:** Compose/Web is Experimental and may be changed at any time. Use it only for evaluation purposes.
We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [GitHub](https://github.com/JetBrains/compose-multiplatform/issues).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.
