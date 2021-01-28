//Pre-request
var user = pm.environment.has("user") ? pm.environment.get("user") : pm.collectionVariables.get("user");
var password = pm.environment.has("password") ? pm.environment.get("password") : pm.collectionVariables.get("password");
console.log("Logging using User: " + user);

if(user == null || user == "" || password == null || password == "") {
        throw new Error("User and Password is required")
}
pm.environment.set("signon_token", "")

//Test
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
    
    var jsonObject = xml2Json(responseBody);
    console.log(JSON.stringify(jsonObject));
    
    if(jsonObject.response.$.result_msg === "success") {
        console.log("Signon Token: " + jsonObject.response.signon_user.token);
        pm.environment.set("signon_token", jsonObject.response.signon_user.token);
    } else {
        pm.setNextRequest(null);
        throw new Error("Unable to authenticate the user");        
    }
    
});
