// 회원가입 미완
const signupButton = document.getElementById('signup-btn');

if (signupButton) {
    signupButton.addEventListener('click', event => {
        fetch('/member', {
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

// 로그인
const loginButton = document.getElementById('login-btn');

if (loginButton) {
    // 등록 버튼을 클릭하면 /api/articles로 요청을 보낸다
    loginButton.addEventListener('click', event => {
        body = JSON.stringify({
            email: document.getElementById('email').value,
            password: document.getElementById('password').value
        });
        function success() {
            alert('로그인에 성공했습니다.');
            location.replace('/board');
        };
        function fail() {
            alert('로그인에 실패했습니다.');
            location.replace('/board');
        };

        httpRequest('GET','/member', body, success, fail)
    });
}