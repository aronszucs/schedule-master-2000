const xhr = new XMLHttpRequest();
xhr.onload = function() {
    if(this.status !== 200) {
        showContents(['login-content']);
    } else {
        showContents(['profile-content', 'logout-content']);
    }
}
xhr.open('GET', 'protected/auth');
xhr.send();