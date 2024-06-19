import { Input } from 'antd';
import React, { useState, useEffect } from 'react';

export const MathCaptcha = ({ onSuccess,onInvalid }) => {
    const [num1, setNum1] = useState(Math.floor(Math.random() * 10));
    const [num2, setNum2] = useState(Math.floor(Math.random() * 10));
    const [userAnswer, setUserAnswer] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    const handleInputChange = (e) => {
        setUserAnswer(e.target.value);
    };

    useEffect(() => {
        setNum1(Math.floor(Math.random() * 10));
        setNum2(Math.floor(Math.random() * 10));
        setUserAnswer('');
        setErrorMessage('');
        setSuccessMessage("");
    }, []);

    useEffect(() => {
        const correctAnswer = num1 + num2;
        if (parseInt(userAnswer) === correctAnswer) {
            onSuccess();
            setErrorMessage("")
            setSuccessMessage("correct answer.");
        } else {
            onInvalid();
            setSuccessMessage("");
            setErrorMessage("wrong answer!!")
        }
        if (userAnswer?.length == 0) {
            setErrorMessage("Please solve the puzzle before submitting.")
        }
    }, [userAnswer, num1, num2, onSuccess]);


    const refreshMathPuzzle = () => {
        setNum1(Math.floor(Math.random() * 10));
        setNum2(Math.floor(Math.random() * 10));
        setUserAnswer('');
        setErrorMessage('');
        setSuccessMessage("");
      };
    
    return (
        <div>
            <div style={{display:"flex",justifyContent:'space-between'}}>
            <p>
                Solve the math problem: {num1} + {num2} = ? 
            </p>
            <p style={{cursor:"pointer"}} onClick={refreshMathPuzzle}>↻</p>
            </div>
            <form>
                <Input
                    className="form-control"
                    type="number"
                    required
                    value={userAnswer}
                    onChange={handleInputChange}
                    placeholder="Your answer"
                />
            </form>
            {errorMessage && <p className='text-danger'>{errorMessage}</p>}
            {successMessage && <p className='text-success'>{successMessage}</p>}
        </div>
    );
};
