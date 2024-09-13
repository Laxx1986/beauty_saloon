// ConfirmModal.js
import React from 'react';
import './ConfirmModal.css';

function ConfirmModal({ show, message, onConfirm, onCancel }) {
    if (!show) {
        return null;
    }

    return (
        <div className="modal-overlay">
            <div className="modal">
                <div className="modal-content">
                    <p>{message}</p>
                    <button className="btn btn-danger" onClick={onConfirm}>Igen</button>
                    <button className="btn btn-secondary" onClick={onCancel}>Nem</button>
                </div>
            </div>
        </div>
    );
}

export default ConfirmModal;
