엑세스토큰은 로컬스토리지에 저장을 해도됨
리프레쉬토큰은 쿠키에 저장을 해도되는데 httpOnly를 사용하면 XSS공격을 방어할 수 있다고함
그럼 엑세스토큰도 쿠키에 저장해도 되는거아님?

JWT 큰 단점 : 이미 발급된 토큰은 서버가 주도권이 없음


