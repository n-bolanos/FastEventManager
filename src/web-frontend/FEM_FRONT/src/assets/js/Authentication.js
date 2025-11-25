async function checkCredentials(username, password) {
    response = await fetch("http://localhost:8070/auth/login",
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

export default async function userRegister(name, username, email, password) {
    try {
        const res = await fetch("http://localhost:8010/auth/register", {
            method: "POST",

            body: JSON.stringify({
                name,
                username,
                email,
                password
            })
        });

        
            console.log(res)
            return res
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}