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
        
    })
})