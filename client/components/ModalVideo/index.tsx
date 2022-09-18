import YouTube, { Options } from 'react-youtube';
import React from 'react';
import { Button, Modal, } from 'react-bootstrap';
import { useState } from 'react';
import styles from "./index.module.scss";
const ModalVideo = ({ step }: any) => {
    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const opts: Options = {
        height: '390',
        width: '640',
        playerVars: {
            autoplay: 1,
        },
    };
    function onReady(event: any) {
        // event.target.pauseVideo();
    }
    return (
        <>
            <div className="col-12 col-lg-3 col-md-6 col-md-6 mb-3 mb-sm-0 intro-extra">
                <div className=" h-100 body-video" onClick={handleShow}>
                    <div className="img-video">
                        <a href="javascript:void(0)">
                            <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" fill="currentColor"
                                className="bi bi-caret-right-fill" viewBox="0 0 16 16">
                                <path
                                    d="m12.14 8.753-5.482 4.796c-.646.566-1.658.106-1.658-.753V3.204a1 1 0 0 1 1.659-.753l5.48 4.796a1 1 0 0 1 0 1.506z" />
                            </svg>
                            <img src={step.image} className="card-img-top rounded-circle shadow"
                                alt="..." loading="lazy" />
                        </a>
                    </div>
                    <div className="card-body text-center">
                        <h5 className="card-title">{step.title}</h5>
                        <p className="card-text">{step.content}</p>
                    </div>
                </div>
                <Modal show={show} size="lg" onHide={handleClose} aria-labelledby="example-modal-sizes-title-sm" centered>
                    <Modal.Body>
                        <div className={styles.videoWrapper}>
                            <YouTube videoId={step.videoId} opts={opts} onReady={onReady} />
                        </div>
                    </Modal.Body>
                </Modal>
            </div>
        </>
    )
}
export default ModalVideo