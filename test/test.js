const assert = require('assert');
const firebase = require('@firebase/testing');

const MY_PROJECT_ID = "findmytag-esc2021";


describe("FindMyTag", () => {

    it("Checking basic maths functions to confirm whether testing is working as expected", () => {
        assert.equal(2+2, 4);
    });

    it("Checking if we can read data from read only collection", () => {
        const db = firebase.initializeTestApp({projectId: MY_PROJECT_ID}).firestore();
        const testDoc = db.collection("readonly").doc("testdock");
        firebase.assertSucceeds(testDoc.get());

    })
    it("Confirming we can't write data from read only collection", () => {
        const db = firebase.initializeTestApp({projectId: MY_PROJECT_ID}).firestore();
        const testDoc = db.collection("readonly").doc("testdock");
        firebase.assertFails(testDoc.set({test: "hello"}));
        
    }
    )
    it("Can't write to a user document with same ID as our user", async() => {
        const myAuth = {uid: "user_abc", email: "abc@gmail.com"}
        const db = firebase.initializeTestApp({projectId: MY_PROJECT_ID, auth: myAuth}).firestore();
        const testDoc = db.collection("users").doc("buser_abc");
        firebase.assertSucceeds(testDoc.set({test: "hello"}));
        
    })
    it("Can't write to a user document with same ID as our user", async() => {
        const myAuth = {uid: "user_abc", email: "abc@gmail.com"}
        const db = firebase.initializeTestApp({projectId: MY_PROJECT_ID, auth: myAuth}).firestore();
        const testDocc = db.collection("users").doc("user_abc");
        firebase.assertSucceeds(testDocc.set({test: "hello"}));
        
    })
    it("Can't write to a user document with different ID as our user", async() => {
        const myAuth = {uid: "usewewr_abc", email: "abwewefc@gmail.com"}
        const db = firebase.initializeTestApp({projectId: MY_PROJECT_ID, auth: myAuth}).firestore();
        const testxDoc = db.collection("users").doc("user_abc");
        firebase.assertFails(testxDoc.set({test: "hello"}));
        
    })
})

