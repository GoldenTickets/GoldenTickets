
// 회원가입
const signupButton = document.getElementById('signup-btn');

if (signupButton) {
    signupButton.addEventListener('click', event => {
        fetch('/signup', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: document.getElementsByName('name')[0].value,
                email: document.getElementsByName('email')[0].value,
                password: document.getElementsByName('password')[0].value,
                nickname: document.getElementsByName('nickname')[0].value
            })
        })
            .then(() => {
                alert('회원가입이 완료되었습니다.');
                location.replace('login');
            });
    });
}