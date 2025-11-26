export async function checkCredentials(username, password) {
    try {
        const res = await fetch("http://localhost:8010/auth/login",
        {method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            identifier: username,
            password: password
        })
        });
        console.log(res)
         return res
    } catch(error){
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function userRegister(name, username, email, password) {
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
            return res
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}