import { useState } from 'react';
import { BsCheck, BsCheckBox, BsX } from 'react-icons/bs';
import styles from './index.module.scss';
const colors = ['#d70606', '#066bd7', '#00a334', '#a41c99', '#ffc900', '#ff7900', '#ff0d9b', '#78be00', '#af5700', '#00bac4'];

const TagForm = ({ index, tagData, onCheckTag, onCancel, onSave, isSubmiting }) => {
  const [isCreating, setIsCreating] = useState(!!tagData);
  const [tag, setTag] = useState(
    tagData
      ? tagData
      : {
        name: '',
        colorCode: '#ddd',
      },
  );
  function onColorChange(colorCode) {
    setTag({
      ...tag,
      colorCode: colorCode,
    });
  }

  function onNameChange(e) {
    setTag({
      ...tag,
      name: e.target.value,
    });
  }

  return (
    <>
      <form className="row checktag_item">
        <div className="col-auto px-2 d-flex align-items-center">
          <div className="checkbox">
            <div className="custom-control custom-checkbox d-inline-block">
              <input
                type="checkbox"
                className="custom-control-input"
                id={`checktag${tag.id}`}
                onChange={e => onCheckTag(e, tag.id)}
                disabled={!isCreating}
              />
              <label className="custom-control-label" htmlFor={`checktag${tag.id}`}></label>
            </div>
          </div>
        </div>
        <div className="col-auto p-0 d-flex align-items-center color_select">
          <svg className="flip_hor" fill={tag.colorCode} viewBox="0 0 24 24" width="22px" height="22px">
            <path d="M0 0h24v24H0V0z" fill="none" />
            <path d="M21.41 11.58l-9-9C12.05 2.22 11.55 2 11 2H4c-1.1 0-2 .9-2 2v7c0 .55.22 1.05.59 1.42l9 9c.36.36.86.58 1.41.58s1.05-.22 1.41-.59l7-7c.37-.36.59-.86.59-1.41s-.23-1.06-.59-1.42zM5.5 7C4.67 7 4 6.33 4 5.5S4.67 4 5.5 4 7 4.67 7 5.5 6.33 7 5.5 7z" />
          </svg>
          <div className={`color_palette shadow ${index == 0 ? 'bottom' : ''}`} style={{ left: isCreating ? 0 : -44 }}>
            {colors.map(colorCode => {
              return (
                <div className={styles.colorItem} key={colorCode}>
                  <span
                    style={{ background: `${colorCode}` }}
                    onClick={() => {
                      onColorChange(colorCode);
                    }}
                  ></span>
                  &nbsp;
                  {colorCode == tag.colorCode ? (
                    <>
                      <BsCheck />
                    </>
                  ) : (
                    ''
                  )}
                </div>
              );
            })}
          </div>
        </div>
        <div className="col pl-2 d-flex align-items-center">
          <input className="newtag_name form-control form-control-sm" placeholder="Tên thẻ" value={tag.name} onChange={e => onNameChange(e)} />
        </div>
        <div className="col-auto pr-1">
          <a
            className="tagedit_btn"
            href="javascript:void(0)"
            title="Hủy bỏ"
            onClick={() => {
              onCancel(tag);
            }}
          >
            <BsX />
          </a>
          <a href="javascript:void(0)" className="tagedit_btn" style={{ pointerEvents: !tag.name || tag.colorCode == "#ddd" || isSubmiting ? 'none' : "" }} title="Lưu lại" onClick={() => onSave(tag)} >
            <BsCheckBox />
          </a>
        </div>
      </form>
    </>
  );
};
export default TagForm;
