import React, { useState } from 'react';
import { Formik } from 'formik';
import * as yup from 'yup';
import { Form } from 'react-bootstrap';
import axios from "helpers/axios";
import { Field } from 'formik';
import { Spinner } from "react-bootstrap";
const schema = yup.object().shape({
    fullName: yup.string().required('Đây là trường bắt buộc').matches(/^([\s]{0,})([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+\s?){1,}([\s]{0,})$/g, "Họ và tên không hợp lệ"),
    phoneNumber: yup.string().required('Đây là trường bắt buộc').matches(/^([\s]{0,})(0)([0-9]){9}([\s]{0,})$/g, 'Số điện thoại không hợp lệ'),
    email: yup.string().required('Đây là trường bắt buộc').matches(/^([\s]{0,})(([^<>()[\]\.!#$%^&*,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]+(\.[^<>()[\]\.!#$%^&*,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.!#$%^&*,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]+\.)+[^<>()[\]\.!#$%^&*,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]{2,})([\s]{0,})$/g, "Email không hợp lệ"),
    companyName: yup.string().required('Đây là trường bắt buộc').matches(/^([\s]{0,})([^\s.]+\s?){1,}([\s]{0,})$/g, 'Đây là trường bắt buộc'),
    content: yup.string().required('Đây là trường bắt buộc').matches(/^([\s]{0,})([^\s.]+\s?){1,}([\s]{0,})$/g, 'Đây là trường bắt buộc'),
})

const ContactForm = ({ changeForm }: any) => {
    const [submitting, setSubmitting] = useState(false);
    const submitFn = async (values: any, { resetForm }: any) => {
        try {
            changeForm(false);
            setSubmitting(true);
            Object.keys(values).forEach((key) => {
                values[key] = values[key].trim();
            });
            const result = (await axios.post('questions', values));
            setSubmitting(false);
            changeForm(true)
            resetForm();

        } catch (error) {
            changeForm(false)
            setSubmitting(false);
        }
    }
    return (
        <>
            <Formik
                initialValues={{
                    fullName: '',
                    phoneNumber: '',
                    email: '',
                    companyName: '',
                    content: ''
                }}
                validationSchema={schema}
                onSubmit={submitFn}
            >
                {({ errors, touched, handleSubmit }) => (
                    <Form onSubmit={handleSubmit}>
                        <div className="row">
                            <div className="col-12 col-md-6">
                                <div className="form-group">
                                    <label htmlFor="fullName">Họ và tên</label>
                                    <Field name="fullName" className={`form-control ${touched.fullName && errors.fullName ? "is-invalid" : ""}`} autoComplete="off" />
                                    {touched.fullName && errors.fullName && <div className="error-feedback">{errors.fullName}</div>}
                                </div>
                            </div>
                            <div className="col-12 col-md-6">
                                <div className="form-group">
                                    <label htmlFor="phoneNumber">Số điện thoại</label>
                                    <Field name="phoneNumber" className={`form-control ${touched.phoneNumber && errors.phoneNumber ? "is-invalid" : ""}`} autoComplete="off" />
                                    {touched.phoneNumber && errors.phoneNumber && <div className="error-feedback">{errors.phoneNumber}</div>}
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-12 col-md-6">
                                <div className="form-group">
                                    <label htmlFor="fullName">Địa chỉ email</label>
                                    <Field name="email" className={`form-control ${touched.email && errors.email ? "is-invalid" : ""}`} autoComplete="off" />
                                    {touched.email && errors.email && <div className="error-feedback">{errors.email}</div>}
                                </div>
                            </div>
                            <div className="col-12 col-md-6">
                                <div className="form-group">
                                    <label htmlFor="companyName">Tên công ty</label>
                                    <Field name="companyName" className={`form-control ${touched.companyName && errors.companyName ? "is-invalid" : ""}`} autoComplete="off" />
                                    {touched.companyName && errors.companyName && <div className="error-feedback">{errors.companyName}</div>}
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-12">
                                <div className="form-group">
                                    <label htmlFor="content">Nội dung câu hỏi</label>
                                    <Field name="content" as="textarea" className={`form-control ${touched.content && errors.content ? "is-invalid" : ""}`} autoComplete="off" />
                                    {touched.content && errors.content && <div className="error-feedback">{errors.content}</div>}
                                </div>
                            </div>
                        </div>
                        <button disabled={submitting} type="submit" className="btn btn_new">
                            {submitting ? <><Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" /> Loading....</> : 'Gửi đi'}
                        </button>
                    </Form>
                )}
            </Formik>

        </>
    )
};

export default ContactForm;