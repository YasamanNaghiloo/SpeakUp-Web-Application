import React, { useState, useEffect } from 'react';
import './CreatePetition.css';

const CreatePetition = () => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    templateString: '',
    goal: '',
    endDate: '',
    category: 'ENVIRONMENTAL',
    locationScope: 'CITY',
    locationCountry: '',
    locationCity: '',
    userId: null, // Initial value as null, will be updated
    responsible: {
      name: '',
      email: '',
      phone: '',
      responsibleTitle: ''
    }
  });

  useEffect(() => {
    let user = null;
    try {
      const userData = localStorage.getItem('user');
      user = userData ? JSON.parse(userData) : null;
      if (user && user.id) {
        setFormData((prev) => ({
          ...prev,
          userId: user.id
        }));
      }
    } catch (e) {
      console.error('Error parsing user from localStorage:', e);
    }
  }, []); // runs once on mount

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (['name', 'email', 'phone', 'responsibleTitle'].includes(name)) {
      setFormData((prev) => ({
        ...prev,
        responsible: {
          ...prev.responsible,
          [name]: value
        }
      }));
    } else {
      setFormData((prev) => {
        const newFormData = { ...prev, [name]: value };
        // Reset location fields when locationScope changes
        if (name === 'locationScope') {
          if (value === 'GLOBAL') {
            newFormData.locationCountry = '';
            newFormData.locationCity = '';
          } else if (value === 'COUNTRY') {
            newFormData.locationCity = '';
          }
        }
        return newFormData;
      });
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Check if user is logged in
    if (!formData.userId) {
      alert("You must be logged in to create a petition.");
      return;
    }

    const petitionData = {
      title: formData.title,
      description: formData.description,
      templateString: formData.templateString,
      goal: parseInt(formData.goal, 10),
      endDate: formData.endDate,
      userId: formData.userId,
      category: formData.category,
      locationScope: formData.locationScope,
      locationCountry: formData.locationCountry,
      locationCity: formData.locationCity,
      responsible: [formData.responsible],
    };

    try {
      console.log("Sending petitionData:", petitionData); // Debug payload
      const response = await fetch("http://localhost:8080/petitions/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(petitionData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.error("Error creating petition:", errorData);
        alert(`Failed to create petition: ${JSON.stringify(errorData)}`);
        return;
      }

      const result = await response.json();
      console.log("Petition created successfully:", result);
      alert("Petition created!");
      // Clear form
      setFormData({
        title: '',
        description: '',
        templateString: '',
        goal: '',
        endDate: '',
        category: 'ENVIRONMENTAL',
        locationScope: 'CITY',
        locationCountry: '',
        locationCity: '',
        userId: null, // Reset to null, will be updated on next mount if logged in
        responsible: {
          name: '',
          email: '',
          phone: '',
          responsibleTitle: ''
        }
      });
    } catch (error) {
      console.error("Unexpected error:", error);
      alert("Unexpected error occurred.");
    }
  };

  return (
    <div className="create-petition-container">
      <h2>Create New Petition</h2>
      <form onSubmit={handleSubmit} className="petition-form">
        <input type="text" name="title" value={formData.title} onChange={handleChange} placeholder="Title" required />
        <textarea name="description" value={formData.description} onChange={handleChange} placeholder="Description" required />
        <textarea name="templateString" value={formData.templateString} onChange={handleChange} placeholder="Template Email Message" required />
        <input name="goal" type="number" value={formData.goal} onChange={handleChange} placeholder="Signature Goal" required />
        <input name="endDate" type="date" value={formData.endDate} onChange={handleChange} required />

        <select name="category" value={formData.category} onChange={handleChange}>
          <option value="ENVIRONMENTAL">Environmental</option>
          <option value="POLITICAL">Political</option>
          <option value="SOCIAL">Social</option>
          <option value="LOCAL">Local</option>
          <option value="OTHER">Other</option>
        </select>

        <select name="locationScope" value={formData.locationScope} onChange={handleChange}>
          <option value="CITY">City</option>
          <option value="COUNTRY">Country</option>
          <option value="GLOBAL">Global</option>
        </select>

        {formData.locationScope === 'COUNTRY' || formData.locationScope === 'CITY' ? (
          <input
            name="locationCountry"
            value={formData.locationCountry}
            onChange={handleChange}
            placeholder="Country"
          />
        ) : null}

        {formData.locationScope === 'CITY' ? (
          <input
            name="locationCity"
            value={formData.locationCity}
            onChange={handleChange}
            placeholder="City"
          />
        ) : null}

        {/* Responsible person section */}
        <input name="name" value={formData.responsible.name} onChange={handleChange} placeholder="Responsible Name" />
        <input name="email" value={formData.responsible.email} onChange={handleChange} placeholder="Responsible Email" />
        <input name="phone" value={formData.responsible.phone} onChange={handleChange} placeholder="Phone Number" />
        <input name="responsibleTitle" value={formData.responsible.responsibleTitle} onChange={handleChange} placeholder="Responsible Title" />

        <button type="submit">Create Petition</button>
      </form>
    </div>
  );
};

export default CreatePetition;
