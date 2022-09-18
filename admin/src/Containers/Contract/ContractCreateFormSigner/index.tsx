import { FieldArray, Formik, Field } from 'formik';
import * as yup from 'yup';
import PerfectScrollbar from 'react-perfect-scrollbar';
import { IconPersonPlus, IconPersonX } from '../../../Components/Icon';
import { useDispatch, useSelector } from 'react-redux';
import { setSigners, setStep } from '@Redux/Actions/CreateContract';
import React from 'react';

declare module 'yup' {
  interface ArraySchema<T> {
    uniqueEmail(message: string): ArraySchema<T>;
  }
}

yup.addMethod(yup.array, 'uniqueEmail', function (message, _mapper = (a: any) => a.email) {
  return this.test('uniqueEmail', message, function (list: any) {
    return list.length === new Set(list.map((a: any) => a.email)).size;
  });
});

const schema = yup.object().shape({
  signers: yup
    .array()
    .of(
      yup.object().shape({
        fullName: yup
          .string()
          .matches(/^([\s]{0,})([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+\s?){1,}([\s]{0,})$/g, 'Họ và tên không hợp lệ')
          .required('Đây là trường bắt buộc'),
        email: yup
          .string()
          .matches(/^([\s]{0,})(([^<>()[\]\\!#$%^&*.,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]+(\.[^<>()[\]\\!#$%^&*.,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))([\s]{0,})$/g, 'Email không hợp lệ')
          .required('Đây là trường bắt buộc'),
        taxCode: yup
          .string()
          .matches(/^([\s]{0,})([0-9]{10}(|-[0-9]{3}))([\s]{0,})$/g, 'Mã số thuế không hợp lệ')
          .required('Đăng là trường bắt buộc'),
      }),
    )
    .uniqueEmail('Email không được trùng nhau.')
    // .required()
    .min(1, 'Mời nhập tối thiểu một đối tác cần ký trong hợp đồng'),
});

const ContractCreateFormSigner = () => {
  const { user } = useSelector((state: any) => state.auth);
  const { signers } = useSelector((state: any) => state.createContract);

  const dispatch = useDispatch();

  const initialValues =
    signers.filter((signer: any) => signer.email == user.email).length == 0
      ? {
        signers: [
          ...[
            {
              index: 0,
              fullName: user.fullName,
              email: user.email,
              taxCode: user.company.taxCode,
            },
          ],
          ...signers,
        ],
      }
      : { signers: signers };

  const submitFn = async (values: any) => {
    const newSigners = values.signers.map((signer: any) => {
      if (signer.position) {
        return signer;
      } else {
        return {
          ...signer,
          ...{
            position: {
              x: 0,
              y: 0,
              percentX: 0,
              percentY: 0,
              page: null,
            },
          },
        };
      }
    });

    dispatch(setSigners(newSigners));
    dispatch(setStep(3));
  };

  const addItem = (values: any, setFieldTouched: any, push: any, _errors: any) => {
    let hasError = false;
    values.signers.forEach((item: any, index: number) => {
      Object.keys(item).forEach(key => {
        setFieldTouched(`signers.${index}.${key}`, true);
        if (key == 'email') {
          if (!/^([\s]{0,})(([^<>()[\]\\!#$%^&*.,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]+(\.[^<>()[\]\\!#$%^&*.,;:àáãạảăắằẳẵặâấầẩẫậèéẹẻẽêềếểễệđìíĩỉịòóõọỏôốồổỗộơớờởỡợùúũụủưứừửữựỳỵỷỹýÀÁÃẠẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẺẼÊỀẾỂỄỆĐÌÍĨỈỊÒÓÕỌỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚŨỤỦƯỨỪỬỮỰỲỴỶỸÝ\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))([\s]{0,})$/g.test(item.email)) {
            hasError = true;
          }
          if (
            item.email &&
            values.signers.filter((e: any, i: any) => {
              return index != i && e.email == item.email;
            }).length > 0
          ) {
            hasError = true;
          }
        }
        if (key == 'taxCode') {
          if (!/^([\s]{0,})([0-9]{10}(|-[0-9]{3}))([\s]{0,})$/g.test(item.taxCode)) {
            hasError = true;
          }
        }
        if (key == 'fullName') {
          if (!item['fullName']) {
            hasError = true;
          } else {
            if (!/^([\s]{0,})([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+\s?){1,}([\s]{0,})$/.test(item.fullName)) {
              hasError = true;
            }
          }
        }
        // if (!item[key]) {
        //   hasError = true;
        // }
      });
    });
    if (!hasError) {
      push({ index: values.signers.length, fullName: '', email: '', taxCode: '' });
    }
  };

  const removeItem = (values: any, remove: any, resetForm: any, index: any, replace: any) => {
    if (values.signers.length > 2) {
      remove(index);
    } else {
      replace(index, {
        index: index,
        fullName: '',
        email: '',
        taxCode: '',
      });
    }
  };
  const onFocus = (event: any) => {
    event.target.setAttribute('autocomplete', 'off');
  }
  return (
    <>

      <Formik initialValues={initialValues} validationSchema={schema} onSubmit={submitFn} autoComplete="off">
        {({ values, touched, errors, setFieldTouched, resetForm, handleSubmit, submitCount, setValues }: any) => (
          <>
            {/* <input type="password" name="password" autoComplete="new-password" hidden></input> */}
            <div className="new_contract_body">
              <div className="ncbody_wrap m-3 my-4 m-md-5 text-center has_ncnav">
                <div className="top_border"></div>

                <div className="nc_body_content">
                  {/* <!-- Different content between steps (Step 2) --> */}
                  <PerfectScrollbar className="partner_wrap p-4">
                    <div className="container-fluid">
                      <FieldArray name="signers">
                        {arrayHelpers => (
                          <div>
                            {values.signers.length > 0 &&
                              values.signers.map((signer: any, index: number) => (
                                <React.Fragment key={index}>
                                  <div className={`partner_area row border rounded py-3 mb-3 ${index < 1 ? 'd-none' : ''}`} key={index}>
                                    <div className="col pr-3">
                                      <Field type="text" autoComplete="off" name={`signers.${index}.fullName`} className={`form-control mb-3 ${(touched.signers?.[index]?.fullName || submitCount > 0) && errors.signers?.[index]?.fullName ? 'is-invalid' : ''}`} placeholder="Họ và tên" />
                                      <div className="row">
                                        <div className="col-12 col-sm pr-sm-0 mb-3 mb-sm-0">
                                          <Field type="text" autoComplete="off" name={`signers.${index}.email`} className={`form-control ${(touched.signers?.[index]?.email || submitCount > 0) && errors.signers?.[index]?.email ? 'is-invalid' : ''}`} placeholder="Địa chỉ email" />
                                        </div>
                                        <div className="col-12 col-sm pl-sm-3">
                                          <Field type="text" autoComplete="off" name={`signers.${index}.taxCode`} className={`form-control ${(touched.signers?.[index]?.taxCode || submitCount > 0) && errors.signers?.[index]?.taxCode ? 'is-invalid' : ''}`} placeholder="Mã số thuế" />
                                        </div>
                                      </div>
                                    </div>
                                    <div className="col-auto pl-0 d-flex align-items-center">
                                      <a className="remove_partner_btn" onClick={() => removeItem(values, arrayHelpers.remove, resetForm, index, arrayHelpers.replace)} data-toggle="tooltip" data-placement="top" title="Xóa đối tác này">
                                        <IconPersonX />
                                      </a>
                                    </div>
                                  </div>
                                </React.Fragment>
                              ))}

                            {!!errors.signers && typeof errors.signers == 'string' ? <div className="text-danger mb-3">{errors.signers}</div> : <></>}
                            {values.signers.length < 5 ? (
                              <div className="text-center">
                                <a className={`btn btn_outline_site add_partner ${values.signers.length == 5 ? 'disabled' : ''}`} onClick={() => addItem(values, setFieldTouched, arrayHelpers.push, errors)}>
                                  <IconPersonPlus style={{ fontSize: '27px' }} />
                                  &nbsp;&nbsp;
                                  <span>THÊM NGƯỜI KÝ</span>
                                </a>
                              </div>
                            ) : (
                              <></>
                            )}
                          </div>
                        )}
                      </FieldArray>
                    </div>
                  </PerfectScrollbar>
                  {/* <!-- End Different content between steps --> */}
                </div>
                <div className="lefttop_corner"></div>
                <div className="leftbottom_corner"></div>
                <div className="righttop_corner"></div>
                <div className="rightbottom_corner"></div>
              </div>
            </div>

            <div className="new_contract_nav text-center py-3">
              <button className="btn btn_outline_site mx-2 mx-sm-3" onClick={() => dispatch(setStep(1))}>
                QUAY LẠI
              </button>
              <button className="btn btn_site mx-2 mx-sm-3" type="submit" onClick={handleSubmit}>
                TIẾP TỤC
              </button>
            </div>

          </>
        )}
      </Formik>
    </>
  );
};

export default ContractCreateFormSigner;
