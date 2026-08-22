export const Card = ({ children, className = '', image = null }) => {
  return (
    <div className={`card ${className}`}>
      {image && (
        <div className="card-image-container mb-4 -mx-8 -mt-8 overflow-hidden rounded-t-[16px]">
          <img src={image} alt="Cover" className="w-full h-48 object-cover hover:scale-105 transition-transform duration-500" />
        </div>
      )}
      {children}
    </div>
  );
};
