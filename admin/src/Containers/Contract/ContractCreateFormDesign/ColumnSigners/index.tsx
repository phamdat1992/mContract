import { Field, FieldArray, Formik } from 'formik';
import { DragEvent, SyntheticEvent, useEffect, useState } from 'react';
import { Form, Modal } from 'react-bootstrap';
import * as yup from 'yup';
import { IconPencilOutline, IconPersonPlus, IconPersonX } from '../../../../Components/Icon';
import styles from './index.module.scss';
import { useSelector, useDispatch } from 'react-redux';
import { setSigners, updateSignerPosition } from '@Redux/Actions/CreateContract';
import SimpleBar from 'simplebar-react';
import { checkDevice } from '../../../../Utils/helpers';

declare module 'yup' {
  interface ArraySchema<T> {
    uniqueEmail(property: string, message: string): ArraySchema<T>;
  }
}
yup.addMethod(yup.array, 'uniqueEmail', function (message) {
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
          .matches(/^([\s]{0,})([aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ]+\s?){1,}([\s]{0,})$/, 'Họ và tên không hợp lệ')
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
    .min(1, ''),
});

const ColumnSigners = ({ simplebarRef, loadedPDF }: any) => {
  const { user } = useSelector((state: any) => state.auth);
  const { signers } = useSelector((state: any) => state.createContract);
  const dispatch = useDispatch();

  const initialValues = {
    signers: signers,
  };

  const [showSignerModal, setShowSignerModal] = useState(false);

  const onSubmitSigners = (values: any) => {
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
    setShowSignerModal(false);
  };

  const addItem = (values: any, setFieldTouched: any, push: any, errors: any) => {
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
      });
    });
    if (!hasError) {
      push({ index: values.signers.length, fullName: '', email: '', taxCode: '' });
      setTimeout(() => {
        const eleComment = document.getElementById(`singerCreate_${values.signers.length}`);
        if (eleComment) {
          eleComment.scrollIntoView({ block: 'center' });
        }
      }, 50)
    }
  };

  const removeItem = (values: any, remove: any, resetForm: any, index: any, replace: any) => {
    if (values.signers.length > 2) {
      remove(index);
    } else {
      // resetForm(index);
      replace(index, {
        index: index,
        fullName: '',
        email: '',
        taxCode: '',
      });
    }
  };

  const onDragStart = (e: DragEvent, index: number) => {
    e.dataTransfer?.setData('index', index.toString());
  };

  const onDragOver = (e: DragEvent) => {
    e.preventDefault();
  };

  const onDrop = (e: DragEvent) => {
    const index = parseInt(e.dataTransfer.getData('index'));
    dispatch(
      updateSignerPosition({
        index: index,
        position: {
          x: 0,
          y: 0,
          percentX: 0,
          percentY: 0,
          page: null,
        },
      }),
    );
  };

  useEffect(() => {
  }, [loadedPDF])

  return (
    <>
      {!checkDevice() ? (
        <SimpleBar className={`col-12 col-md signers pl-2 pl-md-4 pr-2 pr-md-3 mt-2 mt-md-3 flex-row`} ref={simplebarRef} onDragOver={e => onDragOver(e)} onDrop={e => onDrop(e)}>
          <button className="btn btn_outline_site add_signer align-items-center py-1 py-md-3 mb-3" title="Thêm người ký" onClick={() => setShowSignerModal(true)}>
            <IconPersonPlus />
          </button>
          {signers
            .filter((signer: any) => !signer.position?.page)
            .map((item: any, index: number) => {
              return (
                <button
                  className={`btn btn-block signer mb-3 align-items-center ${item.email == user.email ? styles.author_signature : ''} ${!loadedPDF ? 'pointer-none' : ''} draggable dropped`}
                  key={`left-item-${item.index}`}
                  id={`left-item-${item.index}`}
                  data-index={item.index}
                  style={{ transform: `translate(${item.position.x}px, ${item.position.y}px)` }}
                  data-x={item.position.x}
                  data-y={item.position.y}
                  title={item.fullName}
                  draggable="true"
                  onDragStart={(e: DragEvent) => onDragStart(e, item.index)}
                >
                  <div className="signer_icon mb-1">
                    <IconPencilOutline />
                  </div>
                  <div>
                    <span className="signer_meta d-block">{item.email == user.email ? 'Chữ ký của tôi' : 'Chữ ký đối tác'}</span>
                    <span className="signer_name d-block">{item.fullName}</span>
                  </div>
                  <div className="clearfix"></div>
                </button>
              );
            })}
          <div className="rightspace_helper d-block d-md-none"></div>
        </SimpleBar>
      ) : (
        <>
          <div className={`col-12 col-md signers has-device pl-2 pl-md-4 pr-2 pr-md-3 mt-2 mt-md-3 flex-row`} onDragOver={e => onDragOver(e)} onDrop={e => onDrop(e)}>
            <button className="btn btn_outline_site add_signer align-items-center py-1 py-md-3 mb-3" title="Thêm người ký" onClick={() => setShowSignerModal(true)}>
              <IconPersonPlus />
            </button>
            {signers
              .filter((signer: any) => !signer.position?.page)
              .map((item: any, index: number) => {
                return (
                  <button
                    className={`btn btn-block signer mb-3 align-items-center ${item.email == user.email ? styles.author_signature : ''} ${!loadedPDF ? 'pointer-none' : ''} draggable dropped`}
                    key={`left - item - ${item.index} `}
                    id={`left - item - ${item.index} `}
                    data-index={item.index}
                    style={{ transform: `translate(${item.position.x}px, ${item.position.y}px)` }}
                    data-x={item.position.x}
                    data-y={item.position.y}
                    title={item.fullName}
                    draggable="true"
                    onDragStart={(e: DragEvent) => onDragStart(e, item.index)}
                  >
                    <div className="signer_icon mb-1">
                      <IconPencilOutline />
                    </div>
                    <div>
                      <span className="signer_meta d-block">{item.email == user.email ? 'Chữ ký của tôi' : 'Chữ ký đối tác'}</span>
                      <span className="signer_name d-block">{item.fullName}</span>
                    </div>
                    <div className="clearfix"></div>
                  </button>
                );
              })}
            <div className="rightspace_helper d-block d-md-none"></div>
          </div>
        </>
      )}
      {/* Start Modal */}
      <Modal
        show={showSignerModal}
        onHide={() => setShowSignerModal(false)}
        backdrop="static"
        keyboard={false}
        scrollable={true}
        size="lg"
        id="signersModal"
        className="create-contract"
        backdropClassName="zIndex-1050"
        dialogClassName="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg"
      >
        <Modal.Header className="d-block text-center">
          <h5 id="signersModalLabel" className="modal-title text-uppercase">
            Chỉnh sửa đối tác ký hợp đồng
          </h5>
        </Modal.Header>
        <Formik initialValues={initialValues} validationSchema={schema} onSubmit={onSubmitSigners}>
          {({ handleSubmit, values, touched, errors, isSubmitting, setFieldTouched, resetForm }: any) => (
            <>
              <Form noValidate onSubmit={handleSubmit}>
                <SimpleBar className={`${styles.max_height_signers} `}>
                  <Modal.Body className={`${styles.signer_body} py-0 py-lg-4 px-lg-5`}>
                    <div className="container-fluid">
                      <FieldArray name="signers">
                        {arrayHelpers => (
                          <div>
                            {values.signers.length > 0 &&
                              (values.signers as Array<any>).map((signer, index) => (
                                <div id={`singerCreate_${index}`} className={`partner_area row border rounded py-3 mb-3 ${index < 1 ? 'd-none' : ''} `} key={index}>
                                  <div className="col pr-3">
                                    <Field type="text" autoComplete="off" name={`signers.${index}.fullName`} className={`form-control mb-3 ${touched.signers?.[index]?.fullName && (errors.signers?.[index] as any)?.fullName ? 'is-invalid' : ''}`} placeholder="Họ và tên" />
                                    <div className="row">
                                      <div className="col-12 col-sm pr-sm-0 mb-3 mb-sm-0">
                                        <Field type="text" autoComplete="off" name={`signers.${index}.email`} className={`form-control ${touched.signers?.[index]?.email && (errors.signers?.[index] as any)?.email ? 'is-invalid' : ''}`} placeholder="Địa chỉ email" />
                                      </div>
                                      <div className="col-12 col-sm pl-sm-3">
                                        <Field type="text" autoComplete="off" name={`signers.${index}.taxCode`} className={`form-control ${touched.signers?.[index]?.taxCode && (errors.signers?.[index] as any)?.taxCode ? 'is-invalid' : ''}`} placeholder="Mã số thuế" />
                                      </div>
                                    </div>
                                  </div>
                                  <div className="col-auto pl-0 d-flex align-items-center">
                                    <a className="remove_partner_btn" onClick={() => removeItem(values, arrayHelpers.remove, resetForm, index, arrayHelpers.replace)} data-toggle="tooltip" data-placement="top" title="Xóa đối tác này">
                                      <IconPersonX />
                                    </a>
                                  </div>
                                </div>
                              ))}

                            {!!errors.signers && typeof errors.signers == 'string' ? <div className="text-danger text-center mb-3">{errors.signers}</div> : <></>}
                            {values.signers.length < 5 ? (
                              <div className="text-center">
                                <a className={`btn btn_outline_site add_partner ${values.signers.length == 5 ? 'disabled' : ''}`} onClick={() => addItem(values, setFieldTouched, arrayHelpers.push, errors)}>
                                  <IconPersonPlus style={{ fontSize: 27 }} />
                                  &nbsp;&nbsp;
                                  <span>THÊM NGƯỜI KÝ</span>
                                </a>
                              </div>
                            ) : (
                              <></>
                            )}
                          </div>
                        )
                        }
                      </FieldArray >
                    </div >
                  </Modal.Body >
                </SimpleBar >

                {/* {!checkDevice() ? (
                  <SimpleBar className={`${styles.max_height_signers}`}>
                    <Modal.Body className={`${styles.signer_body} py-0 py-lg-4 px-lg-5`}>
                      <div className="container-fluid">
                        <FieldArray name="signers">
                          {arrayHelpers => (
                            <div>
                              {values.signers.length > 0 &&
                                (values.signers as Array<any>).map((signer, index) => (
                                  <div className={`partner_area row border rounded py-3 mb-3 ${index < 1 ? 'd-none' : ''}`} key={index}>
                                    <div className="col pr-3">
                                      <Field type="text" autoComplete="off" name={`signers.${index}.fullName`} className={`form-control mb-3 ${touched.signers?.[index]?.fullName && (errors.signers?.[index] as any)?.fullName ? 'is-invalid' : ''}`} placeholder="Họ và tên" />
                                      <div className="row">
                                        <div className="col-12 col-sm pr-sm-0 mb-3 mb-sm-0">
                                          <Field type="text" autoComplete="off" name={`signers.${index}.email`} className={`form-control ${touched.signers?.[index]?.email && (errors.signers?.[index] as any)?.email ? 'is-invalid' : ''}`} placeholder="Địa chỉ email" />
                                        </div>
                                        <div className="col-12 col-sm pl-sm-3">
                                          <Field type="text" autoComplete="off" name={`signers.${index}.taxCode`} className={`form-control ${touched.signers?.[index]?.taxCode && (errors.signers?.[index] as any)?.taxCode ? 'is-invalid' : ''}`} placeholder="Mã số thuế" />
                                        </div>
                                      </div>
                                    </div>
                                    <div className="col-auto pl-0 d-flex align-items-center">
                                      <a className="remove_partner_btn" onClick={() => removeItem(values, arrayHelpers.remove, resetForm, index, arrayHelpers.replace)} data-toggle="tooltip" data-placement="top" title="Xóa đối tác này">
                                        <IconPersonX />
                                      </a>
                                    </div>
                                  </div>
                                ))}

                              {!!errors.signers && typeof errors.signers == 'string' ? <div className="text-danger text-center mb-3">{errors.signers}</div> : <></>}
                              {values.signers.length < 5 ? (
                                <div className="text-center">
                                  <a className={`btn btn_outline_site add_partner ${values.signers.length == 5 ? 'disabled' : ''}`} onClick={() => addItem(values, setFieldTouched, arrayHelpers.push, errors)}>
                                    <IconPersonPlus style={{ fontSize: 27 }} />
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
                    </Modal.Body>
                  </SimpleBar>
                ) : (
                  <div className={`${styles.max_height_signers}`}>
                    <Modal.Body className={`${styles.signer_body} py-0 py-lg-4 px-lg-5`}>
                      <div className="container-fluid">
                        <FieldArray name="signers">
                          {arrayHelpers => (
                            <div>
                              {values.signers.length > 0 &&
                                (values.signers as Array<any>).map((signer, index) => (
                                  <div className={`partner_area row border rounded py-3 mb-3 ${index < 1 ? 'd-none' : ''}`} key={index}>
                                    <div className="col pr-3">
                                      <Field type="text" autoComplete="off" name={`signers.${index}.fullName`} className={`form-control mb-3 ${touched.signers?.[index]?.fullName && (errors.signers?.[index] as any)?.fullName ? 'is-invalid' : ''}`} placeholder="Họ và tên" />
                                      <div className="row">
                                        <div className="col-12 col-sm pr-sm-0 mb-3 mb-sm-0">
                                          <Field type="text" autoComplete="off" name={`signers.${index}.email`} className={`form-control ${touched.signers?.[index]?.email && (errors.signers?.[index] as any)?.email ? 'is-invalid' : ''}`} placeholder="Địa chỉ email" />
                                        </div>
                                        <div className="col-12 col-sm pl-sm-3">
                                          <Field type="text" autoComplete="off" name={`signers.${index}.taxCode`} className={`form-control ${touched.signers?.[index]?.taxCode && (errors.signers?.[index] as any)?.taxCode ? 'is-invalid' : ''}`} placeholder="Mã số thuế" />
                                        </div>
                                      </div>
                                    </div>
                                    <div className="col-auto pl-0 d-flex align-items-center">
                                      <a href="javascript:void(0)" className="remove_partner_btn" onClick={() => removeItem(values, arrayHelpers.remove, resetForm, index, arrayHelpers.replace)} data-toggle="tooltip" data-placement="top" title="Xóa đối tác này">
                                        <IconPersonX />
                                      </a>
                                    </div>
                                  </div>
                                ))}

                              {!!errors.signers && typeof errors.signers == 'string' ? <div className="text-danger text-center mb-3">{errors.signers}</div> : <></>}
                              {values.signers.length < 5 ? (
                                <div className="text-center">
                                  <a href="javascript:void(0)" className={`btn btn_outline_site add_partner ${values.signers.length == 5 ? 'disabled' : ''}`} onClick={() => addItem(values, setFieldTouched, arrayHelpers.push, errors)}>
                                    <IconPersonPlus style={{ fontSize: 27 }} />
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
                    </Modal.Body>
                  </div>
                )} */}
                < Modal.Footer className="new_contract_nav d-block text-center" >
                  <button type="button" className="btn btn_outline_site mx-2 mx-sm-3" onClick={() => setShowSignerModal(false)}>
                    HỦY BỎ
                  </button>
                  <button type="submit" className="btn btn_site mx-2 mx-sm-3" disabled={isSubmitting}>
                    LƯU LẠI
                  </button>
                </Modal.Footer >
              </Form >
            </>
          )}
        </Formik >
      </Modal >
      {/* End Modal */}
    </>
  );
};
export default ColumnSigners;
