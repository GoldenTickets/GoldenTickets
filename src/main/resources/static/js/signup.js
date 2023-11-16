// 회원가입
const signupButton = document.getElementById('signup-btn');

if (signupButton) {
    signupButton.addEventListener('click', event => {
        fetch('/api/signup', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: document.getElementById('name').value,
                sex: document.querySelector('input[name="sex"]:checked').value,
                phone: document.getElementById('phone').value,
                address: document.getElementById('address').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
                nickname: document.getElementById('nickname').value
            })
        })
            .then(() => {
                alert('회원가입이 완료되었습니다.');
                location.replace('loginpage');
            });
    });
}