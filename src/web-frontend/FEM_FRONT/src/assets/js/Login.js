async function checkCredentials(username, password) {
    response = await fetch("localhost:8070/auth/login",
        {method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            identifier: username,
            password: password
        })
        })
        .then(res => res.json())
        .catch(err => console.error(err));
    
    return res
}