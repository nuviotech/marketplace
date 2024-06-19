// Function to encrypt a string
export const encryptString = (plainText) => {
  try {
    const encryptionKey = process.env.NEXT_PUBLIC_ENCRYPTION_KEY;
    let encryptedText = '';
    for (let i = 0; i < plainText.length; i++) {
      encryptedText += String.fromCharCode(plainText.charCodeAt(i) ^ encryptionKey.charCodeAt(i % encryptionKey.length));
    }
    return encryptedText;
  } catch (error) {
    console.log("error (C_99): " + error);
    return error;
  }
};

// Function to decrypt an encrypted string
export const decryptString = (encryptedText) => {
  try {
    const encryptionKey = process.env.NEXT_PUBLIC_ENCRYPTION_KEY;
    let decryptedText = '';
    for (let i = 0; i < encryptedText.length; i++) {
      decryptedText += String.fromCharCode(encryptedText.charCodeAt(i) ^ encryptionKey.charCodeAt(i % encryptionKey.length));
    }
    return decryptedText;
  } catch (error) {
    console.log("error (C_99): " + error);
    return error;
  }
};

export const getDataFromLocalStorage = (attrName) => {
  try {
    if (typeof window !== 'undefined' && window.localStorage) {
      // Your code that uses localStorage goes here
      const savedDetails = window.localStorage.getItem(attrName);
      const dec =decryptString(savedDetails) 
      return savedDetails ? dec : null;

    } else {
      console.error('localStorage is not available in this environment.');
      alert("localStorage is not available in this environment")
    }
  } catch (error) {
    console.log("error (C_257) : " + error)
  }
}
