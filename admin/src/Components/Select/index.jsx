import { useEffect, useRef, useState } from 'react';

function Select({ optionData, currentValue, optionValue, optionText, register, fieldName, validators, onChange, ...props }) {
  const [value, setValue] = useState(currentValue);
  const [data, setData] = useState(optionData);
  const field = register(fieldName, validators);

  function defaultText(data) {
    // return !data || data.length == 0 ? 'Đang tải dữ liệu...' : 'Vui lòng chọn';
    return 'Vui lòng chọn';
  }

  useEffect(() => {
    setValue(currentValue);
  }, [currentValue]);
  useEffect(() => {
    setData(optionData);
  }, [optionData]);

  return (
    <select
      defaultValue={value}
      {...props}
      {...register(fieldName)}
      onChange={e => {
        field.onChange(e);
        if (onChange) {
          onChange(e.target.value);
        }
      }}
    >
      <option value="">
        {defaultText(data)}
      </option>
      {data.map(item => (
        <option key={optionValue(item)} value={optionValue(item)} selected={value == optionValue(item)} >
          {optionText(item)}
        </option>
      ))}
    </select>

  );
}
export default Select;
